package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.entity.Role;
import com.fzipp.pay.mapper.RoleMapper;
import com.fzipp.pay.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRolesOfIdName() {
        List<Role> roles = roleMapper.getRolesOfIdName();
        return roles;
    }

    @Override
    public boolean addRole(Role role) {
        int i = roleMapper.addRole(role);
        return i>0?true:false;
    }
}
