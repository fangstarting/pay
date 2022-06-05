package com.fzipp.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fzipp.pay.entity.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT * FROM role")
    List<Role> getRoles();

    @Select("SELECT roleId,rname FROM role")
    List<Role> getRolesOfIdName();

    @Insert("INSERT INTO role(rname,notes,updatetime) VALUES(#{rname},#{notes},#{updatetime})")
    int addRole(Role role);

}
