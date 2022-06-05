package com.fzipp.pay.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.utils.PojoToQWrapperUtil;
import com.fzipp.pay.entity.Position;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.PositionService;
import com.fzipp.pay.service.UserService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import javafx.geometry.Pos;
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
@RequestMapping("/position")
public class PositionController {

    private static final String HEAD_PATH = "/position/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @Autowired
    private PositionService positionService;

    @RequestMapping("/addposition")
    public Map<String,Object> addPosition(@RequestBody Position position){
        Map<String,Object> map = new HashMap<>();
        boolean b = positionService.addPosition(position);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        return map;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Position position){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/position/add";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            position.setUpdatetime(new Date());
            positionService.save(position);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/update")
    public Result upPosition(@RequestBody Position position){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/position/update";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            position.setUpdatetime(new Date());
            positionService.updateById(position);
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
        int count = userService.count(new QueryWrapper<User>().eq("positionId", id));
        if (count>0){
            result.setError(ErrorCode.FAULT);
            result.setMessage("职位存在员工信息无法删除！");
            return result;
        }
        try {
            positionService.removeById(id);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/getinbydeptid")
    public Map<String,Object> getINByDeptId(Integer deptId){
        Map<String,Object> map = new HashMap<>();
        List<Position> insByDeptId = positionService.getINSByDeptId(deptId);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("positions",insByDeptId);
        return map;
    }

    @RequestMapping("/getpositions")
    public Map<String,Object> getPositions(){
        Map<String,Object> map = new HashMap<>();
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        List<Position> list = positionService.list();
        map.put("positions",list);
        return map;
    }

    @RequestMapping("/findpositions")
    public Map<String,Object> getPositions(@RequestBody Map o){
        Map<String,Object> map = new HashMap<>();
        Integer pageNum;
        Integer pageSize;
        try {
            pageNum = (Integer) o.get("pageNum");
            pageSize = (Integer) o.get("pageSize");
        } catch (Exception e) {
            pageNum = 1;
            pageSize = 20;
        }
        Position position = JSON.parseObject(JSON.toJSONString(o), Position.class);
        System.out.println(position);
        QueryWrapper<Position> wrapper = new QueryWrapper<>();
        QueryWrapper qWrapperOfPojo = PojoToQWrapperUtil.getQWrapperOfPojo(position, wrapper);
        Page page = positionService.page(new Page<>(pageNum, pageSize), qWrapperOfPojo);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("page",page);
        return map;
    }
}

