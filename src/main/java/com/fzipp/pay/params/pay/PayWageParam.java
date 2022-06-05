package com.fzipp.pay.params.pay;

public class PayWageParam {
    private String dateStr; //yyyy-MM
    private Integer daySum; //考核天数
    private Boolean cover; //是否覆盖

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Integer getDaySum() {
        return daySum;
    }

    public void setDaySum(Integer daySum) {
        this.daySum = daySum;
    }

    public Boolean getCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }
}
