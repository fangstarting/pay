package com.fzipp.pay.controller;

import com.fzipp.pay.common.constant.*;
import com.fzipp.pay.common.utils.LocalHostUtil;
import com.fzipp.pay.entity.SysConfigs;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.SysConfigsService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.*;

@RestController
@RequestMapping("/sysConfigs")
public class SysConfigsController {

    private static final String HEAD_PATH = "/sysConfigs/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @Autowired
    private SysConfigsService sysConfigsService;
    @Value("${path.down.resourceLast}")
    private String resourceLast;

    @GetMapping("/list")
    public Result list(){
        Result result = new Result();
        String mappingPath = "/sysConfigs/list";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            List<SysConfigs> list = sysConfigsService.list();
            result.setData(list);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/videoHomePath")
    public Result getVideoPath() {
        Result result = new Result();  //http://192.168.100.7:8081/payService/load/static/resource?path=/video/wudao1002/wudao1002.mp4
        String path = sysConfigsService.getById(SystemKey.VIDEO_HOME).getSysValueString();
        try {
            path = SysProp.HTTP_TOP + LocalHostUtil.getLocalIP() + resourceLast + "?path=" + path;
            result.setError(ErrorCode.CORRECT);
            result.setData(path);
        } catch (UnknownHostException e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/timeConfigs")
    public Result getTimeConfigs(){
        Result result = new Result();
        try {
            List<SysConfigs> list = new ArrayList<>();
            list.add(sysConfigsService.getById(SystemKey.WORK_UP_TIME));
            list.add(sysConfigsService.getById(SystemKey.WORK_UP_START));
            list.add(sysConfigsService.getById(SystemKey.WORK_UP_END));
            list.add(sysConfigsService.getById(SystemKey.WORK_DOWN_TIME));
            list.add(sysConfigsService.getById(SystemKey.WORK_DOWN_START));
            list.add(sysConfigsService.getById(SystemKey.WORK_DOWN_END));
            result.setError(ErrorCode.CORRECT);
            result.setData(list);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }



    @PostMapping("/update")
    public Result update(@RequestBody SysConfigs sysConfigs){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/sysConfigs/update";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            sysConfigs.setUpdateTime(new Date());
            result.setSign(sysConfigsService.updateById(sysConfigs));
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

}
