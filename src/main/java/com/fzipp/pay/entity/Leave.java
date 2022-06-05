package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
@TableName("leavex")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假Id
     */
    @TableId(value = "leaveId", type = IdType.AUTO)
    private Integer leaveId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 请假开始日期(yyyy-mm-dd)
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startdate;
    /**
     * 请假结束日期(yyyy-mm-dd)
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date enddate;
    /**
     * 请假类型(1病假 2事假)
     */
    private Integer type;
    /**
     * 状态(0 未审核 1 审核通过 2 审核失败)
     */
    private Integer status;
    /**
     * 原因描述
     */
    private String notes;
    /**
     * 审批意见
     */
    private String opinion;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "leaveId=" + leaveId +
                ", userId=" + userId +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", type=" + type +
                ", status=" + status +
                ", notes=" + notes +
                ", opinion=" + opinion +
                ", updatetime=" + updatetime +
                "}";
    }
}
