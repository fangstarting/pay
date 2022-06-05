package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface RoleService extends IService<Role> {

    /**
     * 返回roles{roleId,rname}
     * @return
     */
    List<Role> getRolesOfIdName();

    /**
     * 添加角色
     * @param role
     * @return
     */
    boolean addRole(Role role);
}
