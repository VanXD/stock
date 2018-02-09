package com.vanxd.stock.vo.maxplus;

import java.util.List;

public class CsgoResult {
    private String msg;
    private String status;
    private String version;
    private List<CsgoEvent> result;

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

    public List<CsgoEvent> getResult() {
        return result;
    }

    public void setResult(List<CsgoEvent> result) {
        this.result = result;
    }
}
