package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-27
 */
@TableName("role_power")
public class RolePower implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联表Id
     */
    @TableId(value = "rpId", type = IdType.AUTO)
    private Integer rpId;
    /**
     * 角色Id
     */
    private Integer roleId;
    /**
     * 权限Id
     */
    private Integer powerId;


    public Integer getRpId() {
        return rpId;
    }

    public void setRpId(Integer rpId) {
        this.rpId = rpId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPowerId() {
        return powerId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }

    @Override
    public String toString() {
        return "RolePower{" +
        "rpId=" + rpId +
        ", roleId=" + roleId +
        ", powerId=" + powerId +
        "}";
    }
}
