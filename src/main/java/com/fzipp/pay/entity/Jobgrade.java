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
public class Jobgrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职级Id
     */
    @TableId(value = "jobgradeId", type = IdType.AUTO)
    private Integer jobgradeId;
    /**
     * 职称
     */
    private String jobtitle;
    /**
     * 职称奖金
     */
    private BigDecimal jobbonus;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getJobgradeId() {
        return jobgradeId;
    }

    public void setJobgradeId(Integer jobgradeId) {
        this.jobgradeId = jobgradeId;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public BigDecimal getJobbonus() {
        return jobbonus;
    }

    public void setJobbonus(BigDecimal jobbonus) {
        this.jobbonus = jobbonus;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Jobgrade{" +
        "jobgradeId=" + jobgradeId +
        ", jobtitle=" + jobtitle +
        ", jobbonus=" + jobbonus +
        ", updatetime=" + updatetime +
        "}";
    }
}
