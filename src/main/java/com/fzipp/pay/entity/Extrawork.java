package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Extrawork implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 加班Id
     */
    @TableId(value = "extraworkId", type = IdType.AUTO)
    private Integer extraworkId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 加班类型Id
     */
    private Integer ewtypeId;
    /**
     * 加班时长(h)
     */
    private BigDecimal duration;
    /**
     * 加班日期(yyyy-mm-dd)
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    /**
     * 审批意见
     */
    private String opinion;
    /**
     * 状态(0 未审核 1 审核通过 2 审核失败)
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getExtraworkId() {
        return extraworkId;
    }

    public void setExtraworkId(Integer extraworkId) {
        this.extraworkId = extraworkId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEwtypeId() {
        return ewtypeId;
    }

    public void setEwtypeId(Integer ewtypeId) {
        this.ewtypeId = ewtypeId;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        return "Extrawork{" +
        "extraworkId=" + extraworkId +
        ", userId=" + userId +
        ", ewtypeId=" + ewtypeId +
        ", duration=" + duration +
        ", date=" + date +
        ", status=" + status +
        ", updatetime=" + updatetime +
        "}";
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
