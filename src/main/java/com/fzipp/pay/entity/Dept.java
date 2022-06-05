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
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门Id
     */
    @TableId(value = "deptId", type = IdType.AUTO)
    private Integer deptId;
    /**
     * 部门名称
     */
    private String dname;
    /**
     * 地址
     */
    private String loc;
    /**
     * 领导Id
     */
    private Integer headuserId;
    /**
     * 描述
     */
    private String notes;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Integer getHeaduserId() {
        return headuserId;
    }

    public void setHeaduserId(Integer headuserId) {
        this.headuserId = headuserId;
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
        return "Dept{" +
        "deptId=" + deptId +
        ", dname=" + dname +
        ", loc=" + loc +
        ", headuserId=" + headuserId +
        ", notes=" + notes +
        ", updatetime=" + updatetime +
        "}";
    }
}
