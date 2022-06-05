package com.fzipp.pay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.entity.Jobgrade;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.JobgradeService;
import com.fzipp.pay.service.UserService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/jobgrade")
public class JobgradeController {

    private static final String HEAD_PATH = "/jobgrade/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JobgradeController.class);

    @Autowired
    private JobgradeService jobgradeService;

    @PostMapping("/add")
    public Result add(@RequestBody Jobgrade jobgrade){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/jobgrade/add";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            jobgrade.setUpdatetime(new Date());
            jobgrade.setJobtitle(jobgrade.getJobtitle().trim());
            jobgradeService.save(jobgrade);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage("职称名称重复！");
            e.printStackTrace();
        }
        return result;
    }
    @PostMapping("/update")
    public Result update(@RequestBody Jobgrade jobgrade){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/jobgrade/update";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            jobgrade.setUpdatetime(new Date());
            jobgradeService.updateById(jobgrade);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @Autowired
    private UserService userService;

    @GetMapping("/remove")
    public Result remove (Integer id){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/position/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        int count = userService.count(new QueryWrapper<User>().eq("jobgradeId", id));
        if (count>0){
            result.setError(ErrorCode.FAULT);
            result.setMessage("职称存在员工信息无法删除！");
            return result;
        }
        try {
            jobgradeService.removeById(id);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/addjobgrade")
    public Map<String,Object> addJobgrade(@RequestBody Jobgrade jobgrade){
        Map<String,Object> map = new HashMap<>();
        boolean b = false;
        try {
            jobgrade.setUpdatetime(new Date());
            b = jobgradeService.save(jobgrade);
        } catch (Exception e) {
            LOGGER.error("addJobgrade,title重复添加失败");
        }
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        return map;
    }

    @RequestMapping("/getjobgradesofit")
    public Map<String,Object> getJobgradesOfIdTitle(){
        Map<String,Object> map = new HashMap<>();
        List<Jobgrade> jobgradesOfIdTitle = jobgradeService.getJobgradesOfIdTitle();
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("jobgrades",jobgradesOfIdTitle);
        return map;
    }

    @GetMapping("/list")
    public Result getList(){
        Result result = new Result();
        try {
            List<Jobgrade> list = jobgradeService.list();
            result.setError(ErrorCode.CORRECT);
            result.setData(list);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/del")
    public Result del(Integer id){
        Result result = new Result();
        try {
            boolean b = jobgradeService.removeById(id);
            result.setSign(b);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.DEL_REF_EX);
        }
        return result;
    }
}

