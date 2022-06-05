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
 * @since 2021-12-28
 */
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核表Id
     */
    @TableId(value = "auditId", type = IdType.AUTO)
    private Integer auditId;
    /**
     * 审核类型Id
     */
    private Integer audtypeId;
    /**
     * 数据表Id(根据审批类型获取对应数据表)
     */
    private Integer dataId;
    /**
     * 提交用户Id
     */
    private Integer submituserId;
    /**
     * 备注
     */
    private String notes;
    /**
     * 状态(0未读 1处理 2搁置)
     */
    private Integer status;
    /**
     * 审核人Id
     */
    private Integer auditoruserId;
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

    public Integer getAudtypeId() {
        return audtypeId;
    }

    public void setAudtypeId(Integer audtypeId) {
        this.audtypeId = audtypeId;
    }

    public Integer getSubmituserId() {
        return submituserId;
    }

    public void setSubmituserId(Integer submituserId) {
        this.submituserId = submituserId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAuditoruserId() {
        return auditoruserId;
    }

    public void setAuditoruserId(Integer auditoruserId) {
        this.auditoruserId = auditoruserId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "auditId=" + auditId +
                ", audtypeId=" + audtypeId +
                ", dataId=" + dataId +
                ", submituserId=" + submituserId +
                ", notes='" + notes + '\'' +
                ", status=" + status +
                ", auditoruserId=" + auditoruserId +
                ", updatetime=" + updatetime +
                '}';
    }
}
