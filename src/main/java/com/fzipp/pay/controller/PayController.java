package com.fzipp.pay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.common.constant.*;
import com.fzipp.pay.common.email.ResultEmail;
import com.fzipp.pay.common.email.SendEmail;
import com.fzipp.pay.common.email.ToEmail;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.OperateDataUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.*;
import com.fzipp.pay.params.pay.PayListsParam;
import com.fzipp.pay.params.pay.PayMyListParam;
import com.fzipp.pay.params.pay.PayWageParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.pay.PayList;
import com.fzipp.pay.service.*;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    private static final String HEAD_PATH = "/pay/**";

    @Autowired
    private PayService payService;

    @Autowired
    private AuthorityCertificationService authorityCertificationService;


    /**
     * 月工资核算
     *
     * @return
     */
    @RequestMapping(value = "/wageAccounting", method = RequestMethod.POST)
    public Result wageAccounting(@RequestBody PayWageParam param) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/pay/wageAccounting";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (aBoolean) {
            if (param.getDaySum() == null || param.getDaySum().equals(0)) {
                result.setError(ErrorCode.FAULT);
                result.setMessage("考勤天数不合法！");
                return result;
            }
            try {
                //验证dateStr是否合法
                String dateString = param.getDateStr() + "-01";
                DateUtil.getDate(dateString, "yyyy-MM-dd");
            } catch (ParseException e) {
                result.setError(ErrorCode.FAULT);
                result.setMessage("日期类型不合法！");
                return result;
            }
            try {
                if (!param.getCover()) {
                    //当月工资是否已经核算过
                    QueryWrapper<Pay> wrapper = new QueryWrapper<>();
                    wrapper.eq("date", param.getDateStr());
                    int count = payService.count(wrapper);
                    if (count > 0) {
                        //当月已核算
                        result.setError(ErrorCode.LATER_ACTION);
                        result.setMessage("当月薪资已经过核算，是否覆盖当月数据并继续执行核算操作？");
                        return result;
                    }
                }

                return payService.wageAccounting(param.getDateStr(), param.getDaySum());
            } catch (Exception e) {
                e.printStackTrace();
                result.setError(ErrorCode.FAULT);
                result.setMessage(Messages.API_500);
            }
        }else {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
        }
        return result;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private JobgradeService jobgradeService;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    public Result getLists(@RequestBody PayListsParam param) {
        Result result = new Result();
        try {
            QueryWrapper<Pay> wrapper = new QueryWrapper<>();
            wrapper.eq(param.getDate() != null && !"".equals(param.getDate()), "date", param.getDate())
                    .eq(param.getUserId() != null, "userId", param.getUserId())
                    .eq(param.getStatus() != null, "status", param.getStatus());
            Page<Pay> page = payService.page(new Page<>(param.getPageNum(), param.getPageSize()), wrapper);
            List<Pay> records = page.getRecords();
            List<PayList> payLists = OperateDataUtil.optionShiftList(records, PayList.class);
            payLists.forEach(e -> {
                QueryWrapper<User> u = new QueryWrapper<>();
                u.select("realname", "deptId", "jobgradeId").eq("userId", e.getUserId());
                User user = userService.getOne(u);
                e.setRealName(user.getRealname());
                QueryWrapper<Dept> d = new QueryWrapper<>();
                d.select("dname").eq("deptId", user.getDeptId());
                e.setDeptName(deptService.getOne(d).getDname());
                e.setJobTitle(jobgradeService.getOne(new QueryWrapper<Jobgrade>()
                        .select("jobtitle").eq("jobgradeId", user.getJobgradeId())).getJobtitle());
            });
            Page<PayList> data = new Page<>();
            BeanUtils.copyProperties(page, data);
            data.setRecords(payLists);
            result.setError(ErrorCode.CORRECT);
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
        }
        return result;
    }

    @PostMapping("/mylist")
    public Result getMyList(@RequestBody PayMyListParam param){
        Result result = new Result();
        try {
            User user = requestUtil.getUser();
            QueryWrapper<Pay> wrapper = new QueryWrapper<>();
            wrapper.eq("userId",user.getUserId())
                    .eq(param.getDate()!=null&&!"".equals(param.getDate()),"date",param.getDate())
                    .eq(param.getStatus()!=null,"status",param.getStatus());
            List<PayList> payLists = OperateDataUtil.optionShiftList( payService.list(wrapper), PayList.class);
            payLists.forEach(e -> {
                e.setRealName(user.getRealname());
                QueryWrapper<Dept> d = new QueryWrapper<>();
                d.select("dname").eq("deptId", user.getDeptId());
                e.setDeptName(deptService.getOne(d).getDname());
                e.setJobTitle(jobgradeService.getOne(new QueryWrapper<Jobgrade>()
                        .select("jobtitle").eq("jobgradeId", user.getJobgradeId())).getJobtitle());
            });
            result.setData(payLists);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/grant")
    @Transactional
    public Result grantPay(@RequestParam(value = "payId") Integer payId) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/pay/grant";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            QueryWrapper<Pay> q = new QueryWrapper<>();
            q.select("userId","date","factpay","idealpay","status").eq("payId",payId);
            Pay pay = payService.getOne(q);
            if (pay == null) {
                result.setError(ErrorCode.FAULT);
                result.setMessage("对象不存在！");
                return result;
            }
            if (SysProp.COMMON_STATUS_OK.equals(pay.getStatus())) {
                result.setError(ErrorCode.FAULT);
                result.setMessage("工资已经发放，请勿重复发放！");
                return result;
            }
            UpdateWrapper<Pay> wrapper = new UpdateWrapper<>();
            wrapper.eq("payId", payId).set("status", SysProp.COMMON_STATUS_OK).set("updatetime",new Date());
            payService.update(wrapper);
            this.paySendEmailOrMes(pay);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @Autowired
    private  AuditlogService auditlogService;
    @Autowired
    private RequestUtil requestUtil;

    @PostMapping("/grants")
    @Transactional
    public Result grantsPay(@RequestBody List<Integer> payIds) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/pay/grants";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            if (payIds.size()==0){
                result.setError(ErrorCode.FAULT);
                result.setMessage(Messages.PARAMS_EX);
                return result;
            }
            int sum = 0;
            for (Integer payId : payIds) {
                QueryWrapper<Pay> q = new QueryWrapper<>();
                q.select("userId","date","factpay","idealpay","status").eq("payId",payId);
                Pay pay = payService.getOne(q);
                if (pay!=null&&SysProp.COMMON_STATUS_NO.equals(pay.getStatus())){
                    UpdateWrapper<Pay> wrapper = new UpdateWrapper<>();
                    wrapper.eq("payId", payId).set("status", SysProp.COMMON_STATUS_OK).set("updatetime",new Date());
                    payService.update(wrapper);
                    this.paySendEmailOrMes(pay);
                }else {
                    sum++;
                }
            }
            result.setError(ErrorCode.CORRECT);
            int success = payIds.size()-sum;
            String mes = "总发放数："+payIds.size()+"位，本次成功发放："+success+"位，"+"此前以已经发放："+sum+"位。";
            String s = LogNotes.SEND_BUTCH_PAY(mes, requestUtil.getUserId());
            Auditlog auditlog = new Auditlog();
            auditlog.setNotes(s);
            auditlog.setUpdatetime(new Date());
            auditlog.setUserId(requestUtil.getUserId());
            auditlogService.save(auditlog);
            result.setMessage(mes);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessService messService;

    private void paySendEmailOrMes(Pay pay) {
        User one = userService.getOne(new QueryWrapper<User>().select("accountId", "realname").eq("userId", pay.getUserId()));
        String mail = accountService.getOne(new QueryWrapper<Account>().select("mail").eq("accountId", one.getAccountId())).getMail();
        ToEmail payToEmail = CommonMethod.getPayToEmail(mail, one.getRealname(), pay.getDate(), pay.getIdealpay().toString(), pay.getFactpay().toString());
        ResultEmail resultEmail = sendEmail.htmlEmail(payToEmail);//发送邮件
        Mess mess = new Mess();
        mess.setUserId(pay.getUserId());
        mess.setUpdatetime(new Date());
        mess.setStatus(SysProp.MESS_STATUS_NO);
        mess.setIsSystem(SysProp.MESS_IS_SYSTEM_YES);
        mess.setTitle(Messages.Pay_MAIL_TITLE);
        String content = resultEmail.getToEmail().getContent();
        mess.setMatter(content);
        messService.save(mess);
    }
}

