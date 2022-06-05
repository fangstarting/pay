package com.fzipp.pay.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.constant.SystemKey;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.Jobgrade;
import com.fzipp.pay.entity.Payconfig;
import com.fzipp.pay.entity.SysConfigs;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.payconfig.PayconfigList;
import com.fzipp.pay.results.payconfig.PayconfigMylist;
import com.fzipp.pay.service.JobgradeService;
import com.fzipp.pay.service.PayconfigService;
import com.fzipp.pay.service.SysConfigsService;
import com.fzipp.pay.service.UserService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/payconfig")
@Slf4j
public class PayconfigController {

    private static final String HEAD_PATH = "/payconfig/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @Autowired
    private PayconfigService payconfigService;
    @Autowired
    private SysConfigsService sysConfigsService;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/mylist")
    public Result getMyList(){
        Result result = new Result();
        try {
            User user = requestUtil.getUser();
            QueryWrapper<Payconfig> wrapper = new QueryWrapper<>();
            wrapper
                    .eq("status", SysProp.COMMON_STATUS_OK)
                    .and(i->i.eq(user.getJobgradeId()!=null,"jobgradeId",user.getJobgradeId()).or().isNull("jobgradeId"));
            List<Payconfig> list = payconfigService.list(wrapper);
            PayconfigMylist payconfigMylist = new PayconfigMylist();
            payconfigMylist.setPayconfigs(list);
            Jobgrade jobgrade = jobgradeService.getOne(new QueryWrapper<Jobgrade>().select("jobtitle", "jobbonus").eq("jobgradeId", user.getJobgradeId()));
            payconfigMylist.setUserId(user.getUserId());
            payconfigMylist.setRealname(user.getRealname());
            payconfigMylist.setWorkage(userService.workYear(user.getUserId(),new Date()));
            payconfigMylist.setBasepay(user.getBasepay());
            payconfigMylist.setJobbonus(jobgrade.getJobbonus());
            payconfigMylist.setJobtitle(jobgrade.getJobtitle());
            result.setError(ErrorCode.CORRECT);
            result.setData(payconfigMylist);
        } catch (Exception e) {
            result.setError(ErrorCode.CORRECT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/list")
    public Result getList() {
        Result result = new Result();
        List<Payconfig> list = null;
        try {
            list = payconfigService.list();
            List<PayconfigList> payconfigLists = optionData(list);
            result.setData(payconfigLists);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @Autowired
    private JobgradeService jobgradeService;

    private List<PayconfigList> optionData(List<Payconfig> list) {
        List<PayconfigList> data = new ArrayList<>();
        for (Payconfig payconfig : list) {
            Jobgrade byId = jobgradeService.getById(payconfig.getJobgradeId());
            PayconfigList payconfigList = JSON.parseObject(JSON.toJSONString(payconfig), PayconfigList.class);
            payconfigList.setJobgrade(byId);
            data.add(payconfigList);
        }
        return data;
    }

    @RequestMapping("/upconfig")
    public Result upConfig(@RequestBody Payconfig payconfig) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/payconfig/upconfig";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            String sysValueString = sysConfigsService.getById(SystemKey.DEFAULT_PAYCONFIG).getSysValueString();
            List<Integer> list = (List<Integer>) JSON.parse(sysValueString);
            String id = payconfig.getPayconfigId().toString();
            for (Integer configId : list) {
                if (id.equals(configId.toString())){
                    result.setError(ErrorCode.FAULT);
                    result.setMessage(Messages.UPDATE_CONFIGS_EX);
                    return result;
                }
            }
            payconfig.setUpdatetime(new Date());
            UpdateWrapper<Payconfig> wrapper = new UpdateWrapper<>();
            wrapper.eq("payconfigId", payconfig.getPayconfigId()).set(payconfig.getJobgradeId()==null,"jobgradeId", null);
            boolean b = payconfigService.update(payconfig,wrapper);
            result.setError(ErrorCode.CORRECT);
            result.setSign(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/addconfig")
    public Result addConfig(@RequestBody Payconfig payconfig) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/payconfig/addconfig";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            payconfig.setUpdatetime(new Date());
            boolean save = payconfigService.save(payconfig);
            result.setError(ErrorCode.CORRECT);
            result.setData(save);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/remove")
    public Result removeConfig(String id) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/payconfig/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            String sysValueString = sysConfigsService.getById(SystemKey.DEFAULT_PAYCONFIG).getSysValueString();
            List<Integer> list = (List<Integer >) JSON.parse(sysValueString);
            for (Integer configId : list) {
                if (id.equals(configId.toString())){
                    result.setError(ErrorCode.FAULT);
                    result.setMessage(Messages.REMOVE_CONFIGS_EX);
                    return result;
                }
            }
            Integer id1 = Integer.valueOf(id);
            boolean b = payconfigService.removeById(id1);
            result.setError(ErrorCode.CORRECT);
            result.setSign(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }
}

