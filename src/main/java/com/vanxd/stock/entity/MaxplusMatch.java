package com.vanxd.stock.entity;

import java.time.LocalDateTime;

public class MaxplusMatch {
    private Integer id;
    private String progress_desc;
    private String title;
    private String sub_title;
    private LocalDateTime end_bid_time;
    private String win_team_name;
    private Integer win_team_score;
    private String lose_team_name;
    private Integer lose_team_score;
    private Integer match_id;
    private Integer category_id;
    private String map_name;

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getMatch_id() {
        return match_id;
    }

    public void setMatch_id(Integer match_id) {
        this.match_id = match_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProgress_desc() {
        return progress_desc;
    }

    public void setProgress_desc(String progress_desc) {
        this.progress_desc = progress_desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public LocalDateTime getEnd_bid_time() {
        return end_bid_time;
    }

    public void setEnd_bid_time(LocalDateTime end_bid_time) {
        this.end_bid_time = end_bid_time;
    }

    public String getWin_team_name() {
        return win_team_name;
    }

    public void setWin_team_name(String win_team_name) {
        this.win_team_name = win_team_name;
    }

    public Integer getWin_team_score() {
        return win_team_score;
    }

    public void setWin_team_score(Integer win_team_score) {
        this.win_team_score = win_team_score;
    }

    public String getLose_team_name() {
        return lose_team_name;
    }

    public void setLose_team_name(String lose_team_name) {
        this.lose_team_name = lose_team_name;
    }

    public Integer getLose_team_score() {
        return lose_team_score;
    }

    public void setLose_team_score(Integer lose_team_score) {
        this.lose_team_score = lose_team_score;
    }
}
