package com.fzipp.pay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.LogNotes;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.LocalHostUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.*;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.user.GetUserInfo;
import com.fzipp.pay.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private RequestUtil requestUtil;

    private final Integer OP_USERID() {
//        Integer opUserId = (Integer) request.getSession().getAttribute("userId");
        Integer opUserId = requestUtil.getUserId();
        if (opUserId == null) opUserId = SysProp.DEFAULT_OP_USERID;
        return opUserId;
    }

    @RequestMapping("/activate")
    @Transactional
    public Map<String, Object> activateUser(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer userId = requestUtil.getUserId();
        /**
         * userData:{
         *           roleId:'',//角色Id
         *           deptId:'',//部门Id
         *           positionId:'',//职位Id
         *           jobgradeId:'',//职级Id
         *           realname:'',//真实姓名
         *           sex:'',//性别
         *           education:'',//学历
         *           phone:'',//电话
         *           birthday:'',//生日
         *           hiredate:''//入职日期
         *         }
         */
        //录入信息->user
        user.setUserId(userId);
        user.setUpdatetime(new Date());
        boolean b = userService.upUserInfo(user);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        if (b) {
            map.put("message", "激活成功，等待人事审核，通过即可登录系统！");
        } else return map;
        //激活信息录入审批记录表
        Audit audit = new Audit();
        audit.setAudtypeId(1);
        audit.setDataId(userId);
        audit.setSubmituserId(user.getUserId());
        audit.setNotes("用户信息激活");
        audit.setStatus(0);
        audit.setUpdatetime(new Date());
        auditService.save(audit);

        return map;
    }

    @RequestMapping("/getall")
    public Map<String, Object> getAll(Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        IPage<User> usersByFinds = null;
        try {
            usersByFinds = userService.getUsersByFinds(new User(), pageNum, pageSize);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("page", usersByFinds);
        return map;
    }

    @RequestMapping("/getusers")
    public Map<String, Object> getUsers(@RequestBody Map o) {
        Map<String, Object> map = new HashMap<>();
        User user = new User();
        Integer pageNum;
        Integer pageSize;
        try {
            pageNum = (Integer) o.get("pageNum");
            pageSize = (Integer) o.get("pageSize");
        } catch (Exception e) {
            pageNum = 1;
            pageSize = 20;
        }
        try {
            Integer userId = (Integer) o.get("userId");
            user.setUserId(userId);
        } catch (Exception e) {
        }
        try {
            String realname = (String) o.get("realname");
            if ("".equals(realname)) realname = null;
            user.setRealname(realname);
        } catch (Exception e) {
        }
        try {
            Integer roleId = (Integer) o.get("roleId");
            user.setRoleId(roleId);
        } catch (Exception e) {
        }
        try {
            Integer deptId = (Integer) o.get("deptId");
            user.setDeptId(deptId);
        } catch (Exception e) {
        }
        try {
            Integer positionId = (Integer) o.get("positionId");
            user.setPositionId(positionId);
        } catch (Exception e) {
        }
        try {
            Integer status = (Integer) o.get("status");
            user.setStatus(status);
        } catch (Exception e) {
        }
        IPage<User> usersByFinds = null;
        try {
            usersByFinds = userService.getUsersByFinds(user, pageNum, pageSize);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("page", usersByFinds);
        return map;
    }

    @Autowired
    private AuditlogService auditlogService;
    @Autowired
    private AccountService accountService;
    @Value("${pay.manage.username}")
    private String manageUsername;

    @Transactional
    @RequestMapping("/upuser")
    public Map<String, Object> upUser(@RequestBody User user, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        try {
            user.setUpdatetime(new Date());
            user.setProfile(null);
            String username = null;
            if(user.getAccountId()!=null) {
                username = accountService.getOne(new QueryWrapper<Account>().select("username").eq("accountId", user.getAccountId())).getUsername();
            }
            if (manageUsername.equals(username)&&(user.getStatus()!=SysProp.COMMON_STATUS_OK||user.getRoleId()!=SysProp.ROLE_MANAGE_KEY)){
                map.put("errorcode", 103);
                map.put("message","您无权变更当前账户的角色信息与用户状态！");
                user.setStatus(null);
                user.setRoleId(null);
            }
            userService.updateById(user);
            //生成系统日志
            Auditlog auditlog = new Auditlog();
            auditlog.setUserId(OP_USERID());
            auditlog.setNotes(LogNotes.UP_USER(OP_USERID(), user.getUserId()));
            auditlog.setUpdatetime(new Date());
            auditlogService.save(auditlog);
        } catch (Exception e) {
            map.put("errorcode", ErrorCode.VERIFY_BIZ(false));
        }
        return map;
    }

    @RequestMapping("/getuserbyid")
    public Map<String, Object> getUserById(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.getById(userId);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("user", user);
        return map;
    }

    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    @Value("${path.down.profile}")
    private String pathDownProfile;
    @Value("${path.down.profileLast}")
    private String pathDownProfileList;

    @RequestMapping("/userinfo")
    public Result getUserInfo() {
        Result result = new Result();
//        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer userId = requestUtil.getUserId();
        try {
            User user = userService.getById(userId);
//            user.setProfile(pathDownProfile+user.getProfile());
            String localIP = LocalHostUtil.getLocalIP();
            localIP = SysProp.HTTP_TOP +localIP+pathDownProfileList;
            user.setProfile(localIP+user.getProfile());
            Role role = roleService.getById(user.getRoleId());
            Dept dept = deptService.getById(user.getDeptId());
            Position position = positionService.getById(user.getPositionId());
            GetUserInfo getUserInfo = new GetUserInfo();
            getUserInfo.setUser(user);
            getUserInfo.setRole(role);
            getUserInfo.setDept(dept);
            getUserInfo.setPosition(position);
            result.setError(ErrorCode.CORRECT);
            result.setData(getUserInfo);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
        }
        return result;
    }

    @PostMapping("/upinfo")
    public Result upInfo(@RequestBody User user) {
        Result result = new Result();
        try {
            if (user.getUserId() == null) {
                user.setUserId(requestUtil.getUserId());
            }
            user.setProfile(null);
            user.setUpdatetime(new Date());
            boolean b = userService.updateById(user);
            result.setError(ErrorCode.CORRECT);
            result.setSign(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/url")
    public Result getProfileUrl(){
        Result result = new Result();
        try {
            User user = userService.getById(requestUtil.getUserId());
            String localIP = LocalHostUtil.getLocalIP();
            localIP = SysProp.HTTP_TOP +localIP+pathDownProfileList;
            user.setProfile(localIP+user.getProfile());
            result.setData(localIP+user.getProfile());
//            result.setData(pathDownProfile+user.getProfile());
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

}

