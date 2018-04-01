package com.example.dxc.xfdemo.network.base;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-5:16 PM.
 * @Version 1.0
 */

public class BaseResponse<T> {
    private String error_code;//返回码，200:正常
    private String reason;//SUCCESSED
    private T result;//结果数据

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "resultcode='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}