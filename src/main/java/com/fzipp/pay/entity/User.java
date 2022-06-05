package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 *
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-27
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;
    /**
     * 账户Id
     */
    private Integer accountId;
    /**
     * 角色Id
     */
    private Integer roleId;
    /**
     * 部门Id
     */
    private Integer deptId;
    /**
     * 职位Id
     */
    private Integer positionId;
    /**
     * 职级Id
     */
    private Integer jobgradeId;
    /**
     * 头像(图片全名)
     */
    private String profile;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 性别
     */
    private String sex;
    /**
     * 学历
     */
    private String education;
    /**
     * 电话
     */
    private String phone;
    /**
     * 基本工资
     */
    private BigDecimal basepay;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 入职日期
     */
    private Date hiredate;
    /**
     * 用户状态(0 停用 1 启用)
     */
    private Integer status;
    /**
     * 最终登录时间
     */
    private Date loginendtime;
    /**
     * 更新时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatetime;

//    @Value("${path.down.profile}")
//    private String pathDownProfile;
//
//    /**
//     * 初始化头像地址
//     *
//     * @return initProfile
//     */
//    public String initProfile() {
//        return this.profile = pathDownProfile + this.profile;
//    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getJobgradeId() {
        return jobgradeId;
    }

    public void setJobgradeId(Integer jobgradeId) {
        this.jobgradeId = jobgradeId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getBasepay() {
        return basepay;
    }

    public void setBasepay(BigDecimal basepay) {
        this.basepay = basepay;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLoginendtime() {
        return loginendtime;
    }

    public void setLoginendtime(Date loginendtime) {
        this.loginendtime = loginendtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", accountId=" + accountId +
                ", roleId=" + roleId +
                ", deptId=" + deptId +
                ", positionId=" + positionId +
                ", jobgradeId=" + jobgradeId +
                ", profile=" + profile +
                ", realname=" + realname +
                ", sex=" + sex +
                ", education=" + education +
                ", phone=" + phone +
                ", basepay=" + basepay +
                ", birthday=" + birthday +
                ", hiredate=" + hiredate +
                ", status=" + status +
                ", loginendtime=" + loginendtime +
                ", updatetime=" + updatetime +
                "}";
    }
}
