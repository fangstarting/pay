package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

public class Mess implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "messId", type = IdType.AUTO)
    private Integer messId;

    private Integer userId;

    private String title;

    private String matter;

    /**
     * 状态(0.未读 1.已读)
     */
    private Integer status;

    private Integer isSystem;

    private Date updatetime;

    public Integer getMessId() {
        return messId;
    }

    public void setMessId(Integer messId) {
        this.messId = messId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
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

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public String toString() {
        return "Mess{" +
                "messId=" + messId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", matter='" + matter + '\'' +
                ", status=" + status +
                ", isSystem=" + isSystem +
                ", updatetime=" + updatetime +
                '}';
    }
}
