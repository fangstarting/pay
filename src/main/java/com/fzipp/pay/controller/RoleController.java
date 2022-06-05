package com.fzipp.pay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.entity.Role;
import com.fzipp.pay.entity.RolePower;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.params.role.RoleParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.role.RoleList;
import com.fzipp.pay.service.PowerService;
import com.fzipp.pay.service.RolePowerService;
import com.fzipp.pay.service.RoleService;
import com.fzipp.pay.service.UserService;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/role")
@Slf4j
public class RoleController {
    private static final String HEAD_PATH = "/role/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private PowerService powerService;
    @Autowired
    private RolePowerService rolePowerService;

    @GetMapping("/list")
    public Result list() {
        Result result = new Result();
        String mappingPath = "/role/list";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            List<Role> list = roleService.list();
            List<RoleList> data = new ArrayList<>();
            list.forEach(e -> {
                RoleList roleList = new RoleList();
                roleList.setRole(e);
                List<RolePower> powers = rolePowerService.list(new QueryWrapper<RolePower>().select("powerId").eq("roleId", e.getRoleId()));
                if (powers.isEmpty()){
                    roleList.setPowerIds(null);
                }else {
                    List<Integer> powerIds = new ArrayList<>();
                    powers.forEach(i -> powerIds.add(i.getPowerId()));
                    roleList.setPowerIds(powerIds);
                }
                data.add(roleList);
            });
            result.setData(data);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/add")
    @Transactional
    public Result add(@RequestBody RoleParam param) {
        Result result = new Result();
        String mappingPath = "/role/add";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            Role role = param.getRole();
            role.setUpdatetime(new Date());
            roleService.save(role);
            List<RolePower> rolePowers = new ArrayList<>();
            param.getPowerIds().forEach(id->{
                RolePower rolePower = new RolePower();
                rolePower.setRoleId(role.getRoleId());
                rolePower.setPowerId(id);
                rolePowers.add(rolePower);
            });
            rolePowerService.saveBatch(rolePowers);
            result.setError(ErrorCode.CORRECT);
        } catch (DuplicateKeyException e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage("当前角色名称已存在！");
        }catch (Exception e){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/update")
    @Transactional
    public Result update(@RequestBody RoleParam param) {
        Result result = new Result();
        String mappingPath = "/role/update";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            Role role = param.getRole();
            role.setUpdatetime(new Date());
            roleService.updateById(role);
            rolePowerService.remove(new QueryWrapper<RolePower>().eq("roleId",role.getRoleId()));
            List<RolePower> rolePowers = new ArrayList<>();
            param.getPowerIds().forEach(e->{
                RolePower rolePower = new RolePower();
                rolePower.setRoleId(role.getRoleId());
                rolePower.setPowerId(e);
                rolePowers.add(rolePower);
            });
            rolePowerService.saveBatch(rolePowers);
            result.setError(ErrorCode.CORRECT);
        } catch (DuplicateKeyException e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage("当前角色名称已存在！");
        }catch (Exception e){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }
    @Autowired
    private UserService userService;
    @GetMapping("/remove")
    public Result remove(Integer id){
        Result result = new Result();
        String mappingPath = "/role/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        int roleId = userService.count(new QueryWrapper<User>().eq("roleId", id));
        if(roleId>0){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.DEL_REF_EX);
            return result;
        }
        try {
            rolePowerService.remove(new QueryWrapper<RolePower>().eq("roleId",id));
            roleService.removeById(id);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/getroles")
    public Map<String, Object> getRoles() {
        Map<String, Object> map = new HashMap<>();
        List<Role> rolesOfIdName = roleService.getRolesOfIdName();
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("roles", rolesOfIdName);
        return map;
    }

    @RequestMapping("/addrole")
    public Map<String, Object> methdoName(@RequestBody Role role) {
        Map<String, Object> map = new HashMap<>();
        role.setUpdatetime(new Date());
        System.out.println(role);
        boolean b = roleService.addRole(role);
        map.put("errorcode", ErrorCode.VERIFY_BIZ(b));
        return map;
    }

}

