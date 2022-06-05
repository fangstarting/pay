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
public class Power implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限Id
     */
    @TableId(value = "powerId", type = IdType.AUTO)
    private Integer powerId;
    /**
     * 权限名称
     */
    private String pname;
    /**
     * 权限路径
     */
    private String path;
    /**
     * 描述
     */
    private String notes;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getPowerId() {
        return powerId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        return "Power{" +
        "powerId=" + powerId +
        ", pname=" + pname +
        ", path=" + path +
        ", notes=" + notes +
        ", updatetime=" + updatetime +
        "}";
    }
}
