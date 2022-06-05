package com.fzipp.pay.results.check;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fzipp.pay.entity.Check;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ExcelTarget("checkList")
public class CheckLists extends Check implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 考勤Id
     */
    @Excel(name = "编号", width = 10)
    private Integer checkId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 姓名
     */
    @Excel(name = "姓名",width = 12)
    private String realname;
    /**
     * 考勤日期(yyyy-mm-dd)
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "考勤日期",width = 20, format = "yyyy-MM-dd")
    private Date date;
    /**
     * 上班打卡时间(HH-mm-ss)
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "上班打卡时间",width = 15, format = "HH:mm:ss",replace = {"无记录_null"})
    private Date earlytime;
    /**
     * 下班打卡时间(HH-mm-ss)
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "下班打卡时间",width = 15, format = "HH:mm:ss",replace = {"无记录_null"})
    private Date latetime;
    /**
     * 迟到(0无 1有)
     */
    @Excel(name = "迟到",width = 10, replace = {"无_0", "有_1","未记录_null"})
    private Integer late;
    /**
     * 早退(0无 1有)
     */
    @Excel(name = "早退",width = 10, replace = {"无_0", "有_1","未记录_null"})
    private Integer early;
    /**
     * 打卡主机Ip
     */
    @Excel(name = "打卡地址",width = 20,replace = {"未记录_null"})
    private String hostIp;
    /**
     * 考勤状态(0正常 1缺勤)
     */
    @Excel(name = "考勤状态",width = 10,replace = {"正常_0", "缺勤_1"})
    private Integer status;
    /**
     * 缺勤类型Id
     */
    private Integer abtypeId;
    /**
     * 缺勤类型
     */
    @Excel(name = "缺勤类型",width = 10)
    private String abtypeName;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间",width = 25,format = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;


}
