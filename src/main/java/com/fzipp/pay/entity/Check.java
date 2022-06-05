package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("checkx")
public class Check implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考勤Id
     */
    @TableId(value = "checkId", type = IdType.AUTO)
    private Integer checkId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 考勤日期(yyyy-mm-dd)
     */
    private Date date;
    /**
     * 上班打卡时间(HH-mm-ss)
     */
    private Date earlytime;
    /**
     * 下班打卡时间(HH-mm-ss)
     */
    private Date latetime;
    /**
     * 迟到(0无 1有)
     */
    private Integer late;
    /**
     * 早退(0无 1有)
     */
    private Integer early;
    /**
     * 打卡主机Ip
     */
    private String hostIp;
    /**
     * 考勤状态(0正常 1缺勤)
     */
    private Integer status;
    /**
     * 缺勤类型Id
     */
    private Integer abtypeId;
    /**
     * 更新时间
     */
    private Date updatetime;


    public Integer getCheckId() {
        return checkId;
    }

    public void setCheckId(Integer checkId) {
        this.checkId = checkId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEarlytime() {
        return earlytime;
    }

    public void setEarlytime(Date earlytime) {
        this.earlytime = earlytime;
    }

    public Date getLatetime() {
        return latetime;
    }

    public void setLatetime(Date latetime) {
        this.latetime = latetime;
    }

    public Integer getLate() {
        return late;
    }

    public void setLate(Integer late) {
        this.late = late;
    }

    public Integer getEarly() {
        return early;
    }

    public void setEarly(Integer early) {
        this.early = early;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAbtypeId() {
        return abtypeId;
    }

    public void setAbtypeId(Integer abtypeId) {
        this.abtypeId = abtypeId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Check{" +
        "checkId=" + checkId +
        ", userId=" + userId +
        ", date=" + date +
        ", earlytime=" + earlytime +
        ", latetime=" + latetime +
        ", late=" + late +
        ", early=" + early +
        ", hostIp=" + hostIp +
        ", status=" + status +
        ", abtypeId=" + abtypeId +
        ", updatetime=" + updatetime +
        "}";
    }
}
