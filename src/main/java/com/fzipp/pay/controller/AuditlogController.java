package com.fzipp.pay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.entity.Auditlog;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.params.PageParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.auditlog.AuditlogList;
import com.fzipp.pay.service.AuditlogService;
import com.fzipp.pay.service.UserService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/auditlog")
public class AuditlogController {

    private static final String HEAD_PATH = "/auditlog/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;
    @Autowired
    private AuditlogService auditlogService;
    @Autowired
    private UserService userService;

    @PostMapping("logs")
    public Result getLogs(@RequestBody PageParam param){
        Result result = new Result();
        try {
            Page<Auditlog> page = auditlogService.page(new Page<>(param.getPageNum(), param.getPageSize()));
            List<Auditlog> records = page.getRecords();
            List<AuditlogList> auditlogLists = new ArrayList<>();
            records.forEach(e->{
                String realname = userService.getOne(new QueryWrapper<User>().select("realname").eq("userId", e.getUserId())).getRealname();
                AuditlogList auditlogList = new AuditlogList();
                BeanUtils.copyProperties(e,auditlogList);
                auditlogList.setRealname(realname);
                auditlogLists.add(auditlogList);
            });
            Page<AuditlogList> data = new Page<>();
            BeanUtils.copyProperties(page,data);
            data.setRecords(auditlogLists);
            result.setError(ErrorCode.CORRECT);
            result.setData(data);
        } catch (BeansException e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/remove")
    public Result del(Integer id){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/auditlog/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            auditlogService.removeById(id);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

}

