package com.fzipp.pay.results;

public class Result {
    private Integer error;
    private Boolean sign;
    private String message;
    private Object data;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    public Result(Integer error, Boolean sign, String message, Object data) {
        this.error = error;
        this.sign = sign;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }

    @Override
    public String toString() {
        return "Result{" +
                "error=" + error +
                ", sign=" + sign +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
