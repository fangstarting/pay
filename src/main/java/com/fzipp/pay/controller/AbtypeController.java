package com.fzipp.pay.controller;


import cn.hutool.db.sql.SqlExecutor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.entity.Abtype;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.AbtypeService;
import com.fzipp.pay.service.SysConfigsService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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
@RequestMapping("/abtype")
public class AbtypeController {

    private static final String HEAD_PATH = "/abtype/**";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbtypeController.class);

    @Autowired
    private AbtypeService abtypeService;
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @GetMapping("/listall")
    public Result getListAll() {
        Result result = new Result();
        try {
            List<Abtype> list = abtypeService.list();
            result.setError(ErrorCode.CORRECT);
            result.setData(list);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Abtype abtype) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/abtype/add";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            abtype.setUpdatetime(new Date());
            abtypeService.save(abtype);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/remove")
    public Result remove(Integer id) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/abtype/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            abtypeService.removeById(id);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }
    @Autowired
    private SysConfigsService sysConfigsService;

    @PostMapping("/update")
    public Result update(@RequestBody Abtype abtype) {
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/abtype/update";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            //default_abtype判断
            String default_abtype = sysConfigsService.getById("default_abtype").getSysValueString();
            List<Integer> ids = (List<Integer>) JSON.parse(default_abtype);
            LOGGER.error(ids.toString());
            for (Integer id : ids) {
                if (id.equals(abtype.getAbtypeId())){
                    result.setError(ErrorCode.FAULT);
                    result.setMessage(Messages.UPDATE_CONFIGS_EX);
                    return result;
                }
            }
            abtype.setUpdatetime(new Date());
            abtypeService.updateById(abtype);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }
}

