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
public class Auditlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核Id
     */
    @TableId(value = "auditId", type = IdType.AUTO)
    private Integer auditId;
    /**
     * 审核人Id
     */
    private Integer userId;
    /**
     * 审核描述
     */
    private String notes;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Auditlog{" +
        "auditId=" + auditId +
        ", userId=" + userId +
        ", notes=" + notes +
        ", updatetime=" + updatetime +
        "}";
    }
}
