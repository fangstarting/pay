package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-27
 */
public class Accountlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户日志Id
     */
    @TableId(value = "alogId", type = IdType.AUTO)
    private Integer alogId;
    /**
     * 账户Id
     */
    private Integer accountId;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 登录地址
     */
    private String loginIp;


    public Integer getAlogId() {
        return alogId;
    }

    public void setAlogId(Integer alogId) {
        this.alogId = alogId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public String toString() {
        return "Accountlog{" +
        "alogId=" + alogId +
        ", accountId=" + accountId +
        ", loginTime=" + loginTime +
        ", loginIp=" + loginIp +
        "}";
    }
}
