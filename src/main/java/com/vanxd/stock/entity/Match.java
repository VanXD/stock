package com.vanxd.stock.entity;

public class Match {
    private String end_bid_time;
    private int match_id;
    private int match_type;
    private String progress;
    private String progress_desc;
    private int result;
    private int sort_order;
    private String sub_title;

    public String getEnd_bid_time() {
        return end_bid_time;
    }

    public void setEnd_bid_time(String end_bid_time) {
        this.end_bid_time = end_bid_time;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getMatch_type() {
        return match_type;
    }

    public void setMatch_type(int match_type) {
        this.match_type = match_type;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getProgress_desc() {
        return progress_desc;
    }

    public void setProgress_desc(String progress_desc) {
        this.progress_desc = progress_desc;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}
