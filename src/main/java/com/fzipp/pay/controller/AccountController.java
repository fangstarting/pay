package com.fzipp.pay.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.LogNotes;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.email.SendEmail;
import com.fzipp.pay.common.redis.cache.RedisClient;
import com.fzipp.pay.common.utils.LocalHostUtil;
import com.fzipp.pay.common.utils.MD5Util;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.common.utils.TokenUtil;
import com.fzipp.pay.entity.Account;
import com.fzipp.pay.entity.Accountlog;
import com.fzipp.pay.entity.Auditlog;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.entity.child.UserLogin;
import com.fzipp.pay.params.account.RegParam;
import com.fzipp.pay.params.account.UpPasswordParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.AccountService;
import com.fzipp.pay.service.AccountlogService;
import com.fzipp.pay.service.AuditlogService;
import com.fzipp.pay.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Value("${path.down.profile}")
    private String pathDownProfile;
    @Value("${path.down.profileLast}")
    private String pathDownProfileList;
    @Autowired
    private HttpSession session;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountlogService accountlogService;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping("/login.action")
    public Map<String,Object> login(HttpServletRequest request, @RequestBody String json){
        Map<String,Object> map = new HashMap<>();
        JSONObject params = JSON.parseObject(json);
        String id = params.getString("id");
        String verCode = params.getString("code");
        if(!accountService.check(id,verCode)){
            map.put("errorcode", 103);
            map.put("message","验证码错误");
            return map;
        }
        String username = params.getString("username");
        String password = params.getString("password");
        Account account = accountService.getAccount(username, password);
        boolean b = account==null?false:true;
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        if(!b){
            map.put("message","账户密码有误或不匹配");
            return map;
        }
        User user = userService.getUser(account.getAccountId());
        boolean b1 = accountService.verifyStatus(account.getAccountId());
        if(!b1){
            map.put("errorcode",ErrorCode.VERIFY_STATUS(b1));
            map.put("message","账户已停用");
            return map;
        }
        //登录成功 记录账户信息

//        request.getSession().setAttribute("username",username);
//        request.getSession().setAttribute("accountId",account.getAccountId());
//        request.getSession().setAttribute("userId",user.getUserId());
//        request.getSession().setAttribute("status",1);
//        request.getSession().setAttribute("user",user);
        //TODO 使用Redis存储
        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setUsername(username);
        userLogin.setAccountId(account.getAccountId());
        userLogin.setLoginStatus(SysProp.COMMON_STATUS_OK);
        LOGGER.info(JSON.toJSONString(userLogin));
        //日志记录
        Accountlog accountlog = new Accountlog();
        accountlog.setAccountId(account.getAccountId());
        accountlog.setLoginTime(new Date());
        accountlog.setLoginIp(request.getRemoteAddr());
        accountlogService.save(accountlog);
        User upLoginEndTimeU = new User();
        upLoginEndTimeU.setUserId(user.getUserId());
        upLoginEndTimeU.setLoginendtime(new Date());
        userService.updateById(upLoginEndTimeU);
        /**返回Token*/
        String token = TokenUtil.sign(username, password);
        //存储信息至Redis 时效1小时
        redisClient.set(token,userLogin,3600);
        map.put("token", token);
//        map.put("token", JwtUtil.createToken(String.valueOf(account.getAccountId()),account.getUsername()));
        //激活状态判定
        if(user.getStatus()==0){
            //未激活
            String code = ErrorCode.VERIFY_ACTIVE(false);
            if(user.getRealname()!=null){
                code=ErrorCode.VERIFY_ACTIVE(true);
            }
            map.put("errorcode",code);
//            request.getSession().setAttribute("userId",user.getUserId());
            Map<String,Object> activateInfo = new HashMap<>();
            activateInfo.put("sex",user.getSex());
            activateInfo.put("birthday",user.getBirthday());
            map.put("activateInfo",activateInfo);
            return map;
        }
        if(user.getStatus()==2){
            map.put("errorcode",123);
            map.put("message","当前用户已被冻结，请联系部门管理员！");
            return map;
        }
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("userId",user.getUserId());
        userInfo.put("roleId",user.getRoleId());
        String localIP = null;
        try {
            localIP = LocalHostUtil.getLocalIP();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        localIP = "http://"+localIP+pathDownProfileList;
        userInfo.put("profile",localIP+user.getProfile());
//        userInfo.put("profile",pathDownProfile+user.getProfile());
        userInfo.put("realname",user.getRealname());
        userInfo.put("sex",user.getSex());
        userInfo.put("birthday",user.getBirthday());
        userInfo.put("updatetime",user.getUpdatetime());
        /**返回用户主要信息*/
        map.put("userInfo",userInfo);
        return map;
    }

    @RequestMapping("/reg.action")
    public Map<String,Object> reg(@RequestBody RegParam param){
        Map<String,Object> map = new HashMap<>();
        String id = param.getId();
        String verifyCode = param.getVerifyCode();
        String email = param.getMail();
        Boolean verifyCodeBoolean = sendEmail.isVerifyCode(id,email,verifyCode);
        if(!verifyCodeBoolean){
            //校验有误
            map.put("message","验证码校验无效！");
            map.put("errorcode", ErrorCode.VERIFY_BIZ(false));
            return map;
        }
        boolean b = accountService.reg(param);
        if(!b){
            map.put("message","用户名已存在！");
        }
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        return map;
    }

    @Autowired
    private AuditlogService auditlogService;

    @Autowired
    private RequestUtil requestUtil;

    @PostMapping("/up1")
    public void up1(@RequestBody Object params){
        System.out.println(params.toString());
    }

    @Transactional
    @RequestMapping("/uppassword")
    public Result upPassword(@RequestBody UpPasswordParam params){
        Result result = new Result();
        try {
            User user = requestUtil.getUser();
            String username = requestUtil.getUsername();
            Account account = accountService.getAccount(username, params.getOldPassword());
            if(account!=null){
                //执行修改密码
                account.setPassword(MD5Util.getPass(params.getNewPassword(),username));
                account.setUpdatetime(new Date());
                boolean b = accountService.updateById(account);
                if(b){
                    Auditlog auditlog = new Auditlog();
                    auditlog.setUpdatetime(new Date());
                    auditlog.setUserId(user.getUserId());
                    auditlog.setNotes(LogNotes.UP_PASSWORD(user.getUserId(),account.getAccountId()));
                    auditlogService.save(auditlog);
                }
                result.setError(ErrorCode.CORRECT);
            }else {
                result.setError(ErrorCode.FAULT);
            }
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }




}

