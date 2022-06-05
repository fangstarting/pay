package com.fzipp.pay.params.mess;

import com.fzipp.pay.params.PageParam;

public class GetMyListParam extends PageParam {

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString()+"GetMyListParam{" +
                "status=" + status +
                '}';
    }

}
