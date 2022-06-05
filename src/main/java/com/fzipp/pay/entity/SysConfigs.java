package com.fzipp.pay.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sys_configs")
public class SysConfigs implements Serializable {

    @TableId(value = "sys_key")
    private String sysKey;

    @TableField("sys_title")
    private String sysTitle;

    @TableField("sys_value_number")
    private BigDecimal sysValueNumber;

    @TableField("sys_value_string")
    private String sysValueString;

    @TableField("update_time")
    private Date updateTime;
}
