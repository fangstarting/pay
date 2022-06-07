package com.fzipp.pay.params.ddos;

/**
 * @program pay
 * @description
 * @Author FFang
 * @Create 2022-06-07 04:42
 */
public class DdosStartParam {
    private String api;//接口地址
    private String type = "GET";//请求类型
    private Long interval = 1000L;//频率->毫秒
    private Long duration = 30L;//时长（秒）
    private Integer concurrent = 1;//并发线程数

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        if (interval==0L) {
            this.interval = 0L;
        }else {
            long a = 1000 / interval;
            if (a < 1) a = 1;
            this.interval = a;
        }
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        if (duration > 0) this.duration = duration;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        if (concurrent > 0) this.concurrent = concurrent > 100 ? 100 : concurrent;
    }
}
