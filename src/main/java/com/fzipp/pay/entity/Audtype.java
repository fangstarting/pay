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
public class Audtype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核类型表Id
     */
    @TableId(value = "audtypeId", type = IdType.AUTO)
    private Integer audtypeId;
    /**
     * 类型所映射的数据表
     */
    private String tablename;
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


    public Integer getAudtypeId() {
        return audtypeId;
    }

    public void setAudtypeId(Integer audtypeId) {
        this.audtypeId = audtypeId;
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

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    @Override
    public String toString() {
        return "Audtype{" +
                "audtypeId=" + audtypeId +
                ", tablename='" + tablename + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", status=" + status +
                ", updatetime=" + updatetime +
                '}';
    }
}
