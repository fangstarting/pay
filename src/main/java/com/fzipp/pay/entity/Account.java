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
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户Id
     */
    @TableId(value = "accountId", type = IdType.AUTO)
    private Integer accountId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 账户状态(0 注销 1正常)
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Account{" +
        "accountId=" + accountId +
        ", username=" + username +
        ", password=" + password +
        ", idCard=" + idCard +
        ", mail=" + mail +
        ", status=" + status +
        ", updatetime=" + updatetime +
        "}";
    }
}
