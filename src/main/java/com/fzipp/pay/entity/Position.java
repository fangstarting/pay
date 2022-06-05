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
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职位Id
     */
    @TableId(value = "positionId", type = IdType.AUTO)
    private Integer positionId;
    /**
     * 部门Id
     */
    private Integer deptId;
    /**
     * 职位名称
     */
    private String pname;
    /**
     * 描述
     */
    private String notes;
    /**
     * 职位状态(0 停用 1启用)
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Position{" +
        "positionId=" + positionId +
        ", deptId=" + deptId +
        ", pname=" + pname +
        ", notes=" + notes +
        ", status=" + status +
        ", updatetime=" + updatetime +
        "}";
    }
}
