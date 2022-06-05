package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("pay")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资Id
     */
    @TableId(value = "payId", type = IdType.AUTO)
    private Integer payId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 工资月份(yyyy-mm)
     */
    private String date;
    /**
     * 实发工资
     */
    private BigDecimal factpay;
    /**
     * 应发工资
     */
    private BigDecimal idealpay;
    /**
     * 基本工资
     */
    private BigDecimal basepay;
    /**
     * 补贴
     */
    private BigDecimal subsidy;
    /**
     * 职称奖金
     */
    private BigDecimal jobbonus;
    /**
     * 工龄工资
     */
    private BigDecimal senioritypay;
    /**
     * 全勤奖金
     */
    private BigDecimal attendance;
    /**
     * 请假天数
     */
    private Integer insure;
    /**
     * 迟到次数
     */
    private Integer late;
    /**
     * 早退次数
     */
    private Integer early;
    /**
     * 缺勤天数
     */
    private Integer absenteeism;
    /**
     * 扣款详情
     */
    private String leavex;
    /**
     * 扣款金额
     */
    private BigDecimal submoney;
    /**
     * 加班费
     */
    private BigDecimal overtime;
    /**
     * 个税
     */
    private BigDecimal tax;
    /**
     * 发放状态(0 未发放 1 已发放)
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updatetime;

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getFactpay() {
        return factpay;
    }

    public void setFactpay(BigDecimal factpay) {
        this.factpay = factpay;
    }

    public BigDecimal getIdealpay() {
        return idealpay;
    }

    public void setIdealpay(BigDecimal idealpay) {
        this.idealpay = idealpay;
    }

    public BigDecimal getBasepay() {
        return basepay;
    }

    public void setBasepay(BigDecimal basepay) {
        this.basepay = basepay;
    }

    public BigDecimal getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(BigDecimal subsidy) {
        this.subsidy = subsidy;
    }

    public BigDecimal getJobbonus() {
        return jobbonus;
    }

    public void setJobbonus(BigDecimal jobbonus) {
        this.jobbonus = jobbonus;
    }

    public BigDecimal getSenioritypay() {
        return senioritypay;
    }

    public void setSenioritypay(BigDecimal senioritypay) {
        this.senioritypay = senioritypay;
    }

    public BigDecimal getAttendance() {
        return attendance;
    }

    public void setAttendance(BigDecimal attendance) {
        this.attendance = attendance;
    }

    public Integer getInsure() {
        return insure;
    }

    public void setInsure(Integer insure) {
        this.insure = insure;
    }

    public Integer getLate() {
        return late;
    }

    public void setLate(Integer late) {
        this.late = late;
    }

    public Integer getEarly() {
        return early;
    }

    public void setEarly(Integer early) {
        this.early = early;
    }

    public Integer getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(Integer absenteeism) {
        this.absenteeism = absenteeism;
    }

    public String getLeavex() {
        return leavex;
    }

    public void setLeavex(String leavex) {
        this.leavex = leavex;
    }

    public BigDecimal getSubmoney() {
        return submoney;
    }

    public void setSubmoney(BigDecimal submoney) {
        this.submoney = submoney;
    }

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(BigDecimal overtime) {
        this.overtime = overtime;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
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
        return "Pay{" +
                "payId=" + payId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", factpay=" + factpay +
                ", idealpay=" + idealpay +
                ", basepay=" + basepay +
                ", subsidy=" + subsidy +
                ", jobbonus=" + jobbonus +
                ", senioritypay=" + senioritypay +
                ", attendance=" + attendance +
                ", insure=" + insure +
                ", late=" + late +
                ", early=" + early +
                ", absenteeism=" + absenteeism +
                ", leavex='" + leavex + '\'' +
                ", submoney=" + submoney +
                ", overtime=" + overtime +
                ", tax=" + tax +
                ", status=" + status +
                ", updatetime=" + updatetime +
                '}';
    }
}
