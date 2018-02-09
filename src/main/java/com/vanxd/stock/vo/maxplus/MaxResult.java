package com.vanxd.stock.vo.maxplus;

import java.util.List;

public class MaxResult {
    private String msg;
    private String status;
    private String version;
    private List<MaxCategory> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<MaxCategory> getResult() {
        return result;
    }

    public void setResult(List<MaxCategory> result) {
        this.result = result;
    }
}
