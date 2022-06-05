package com.fzipp.pay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SystemKey;
import com.fzipp.pay.entity.Dept;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.entity.child.DeptInfo;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.DeptService;
import com.fzipp.pay.service.UserService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/dept")
public class DeptController {

    private static final String HEAD_PATH = "/dept/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;
    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @RequestMapping("/add")
    public Result add(@RequestBody Dept dept){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/dept/add";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            if (dept.getDname()==null||"".equals(dept.getDname())){
                result.setError(ErrorCode.FAULT);
                result.setMessage("部门名称不能为空，请重新添加！");
                return result;
            }
            List<Dept> deptList = deptService.list(new QueryWrapper<Dept>().select("dname"));
            for (Dept d : deptList) {
                if (d.getDname().equals(dept.getDname())){
                    result.setError(ErrorCode.FAULT);
                    result.setMessage("部门名称已存在，请重新添加！");
                    return result;
                }
            }
            if(this.isDeptHead(dept.getHeaduserId())){
                result.setError(ErrorCode.FAULT);
                result.setMessage("部门领导无效，请重新添加！");
                return result;
            }
            dept.setUpdatetime(new Date());
            deptService.save(dept);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Dept dept){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/dept/update";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            if (dept.getDname()==null||"".equals(dept.getDname())){
                result.setError(ErrorCode.FAULT);
                result.setMessage("部门名称不能为空，请重新添加！");
                return result;
            }
            if(this.isDeptHead(dept.getHeaduserId())){
                result.setError(ErrorCode.FAULT);
                result.setMessage("部门领导无效，请重新添加！");
                return result;
            }
            deptService.updateById(dept);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 部门领导合法性判断
     * @param headId
     * @return true:不合法
     */
    private boolean isDeptHead(Integer headId){
        if (headId==null)return true;
        List<User> userList = userService.list(new QueryWrapper<User>().select("userId").eq("roleId", SystemKey.ROLE_DEPT_MANAGER));
        for (User user : userList) {
            if (user.getUserId().equals(headId)){
                return false;
            }
        }
        return true;
    }

    @GetMapping("/remove")
    public Result remove(Integer id){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/dept/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            int d = userService.count(new QueryWrapper<User>().eq("deptId", id));
            if (d>0){
                result.setError(ErrorCode.FAULT);
                result.setMessage("部门存在员工信息无法删除！");
                return result;
            }
            deptService.removeById(id);
             result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/managers")
    public Result getManagers(){
        Result result = new Result();
        try {
            List<User> userList = userService.list(new QueryWrapper<User>().select("userId","realname").eq("roleId", SystemKey.ROLE_DEPT_MANAGER));
            result.setError(ErrorCode.CORRECT);
            result.setData(userList);
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
        }
        return result;
    }

    @RequestMapping("/adddept")
    public Map<String,Object> methdoName(@RequestBody Dept dept){
        Map<String,Object> map = new HashMap<>();
        dept.setUpdatetime(new Date());
        boolean b = deptService.addDept(dept);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        return map;
    }

    @RequestMapping("/getdepts")
    public Map<String,Object> getDepts(){
        Map<String,Object> map = new HashMap<>();
        List<Dept> deptsOfIdName = deptService.getDeptsOfIdName();
        map.put("errorcode",ErrorCode.VERIFY_BIZ(true));
        map.put("depts",deptsOfIdName);//TODO 哈哈？
        return map;
    }

    @RequestMapping("/getdeptinfos")
    public Map<String,Object> getDeptInfos(@RequestBody(required = false) Dept dept){
        if (dept==null)dept=new Dept();
        Map<String,Object> map = new HashMap<>();
        List<DeptInfo> deptInfos = deptService.getDeptInfos(dept);
        map.put("errorcode",ErrorCode.VERIFY_BIZ(true));
        map.put("deptInfos",deptInfos);
        return map;
    }
}

