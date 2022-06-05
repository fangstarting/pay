package com.fzipp.pay.params.check;

import lombok.Data;


@Data
public class CheckEchartsStatParam {
    /**
     * 1.日期范围查询
     * 2.月份范围查询
     * 3.年份范围查询
     */
    private Integer type;

    /**
     * 开始时间  yyyy-MM-dd / yyyy-MM / yyyy
     */
    private String startDate;
    /**
     * 结束时间 yyyy-MM-dd / yyyy-MM / yyyy
     */
    private String endDate;

}
