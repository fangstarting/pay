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
public class Payconfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资配置表Id
     */
    @TableId(value = "payconfigId", type = IdType.AUTO)
    private Integer payconfigId;
    /**
     * 薪资名称
     */
    private String name;
    /**
     * 薪资类型(增加or减少)
     */
    private String type;
    /**
     * 计算规则(直接or计算)
     */
    private String rule;
    /**
     * 涉及金额
     */
    private BigDecimal money;
    /**
     * 计算表达式
     */
    private String expression;
    /**
     * 状态(0 停用 1 启用)
     */
    private Integer status;
    /**
     * 职级Id(等级)
     */
    private Integer jobgradeId;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getPayconfigId() {
        return payconfigId;
    }

    public void setPayconfigId(Integer payconfigId) {
        this.payconfigId = payconfigId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getJobgradeId() {
        return jobgradeId;
    }

    public void setJobgradeId(Integer jobgradeId) {
        this.jobgradeId = jobgradeId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Payconfig{" +
                "payconfigId=" + payconfigId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", rule='" + rule + '\'' +
                ", money=" + money +
                ", expression='" + expression + '\'' +
                ", status=" + status +
                ", jobgradeId=" + jobgradeId +
                ", updatetime=" + updatetime +
                '}';
    }
}
