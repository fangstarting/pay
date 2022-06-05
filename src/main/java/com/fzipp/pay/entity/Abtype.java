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
public class Abtype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 缺勤类型Id
     */
    @TableId(value = "abtypeId", type = IdType.AUTO)
    private Integer abtypeId;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 注释说明
     */
    private String notes;
    /**
     * 状态(0停用 1启用)
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getAbtypeId() {
        return abtypeId;
    }

    public void setAbtypeId(Integer abtypeId) {
        this.abtypeId = abtypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Abtype{" +
        "abtypeId=" + abtypeId +
        ", name=" + name +
        ", notes=" + notes +
        ", status=" + status +
        ", updatetime=" + updatetime +
        "}";
    }
}
