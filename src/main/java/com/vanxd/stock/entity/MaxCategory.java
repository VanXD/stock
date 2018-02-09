package com.vanxd.stock.entity;

import java.util.List;

public class MaxCategory {
    private int category_id;
    private String category_type;
    private String end_bid_time;
    private String league_img_url;
    private int match_id;
    private String progress;
    private String progress_desc;
    private int sort_order;
    private TeamInfo team1_info;
    private int team1_score;
    private TeamInfo team2_info;
    private int team2_score;
    private String title;
    private int top;
    private List<Match> matches;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public String getEnd_bid_time() {
        return end_bid_time;
    }

    public void setEnd_bid_time(String end_bid_time) {
        this.end_bid_time = end_bid_time;
    }

    public String getLeague_img_url() {
        return league_img_url;
    }

    public void setLeague_img_url(String league_img_url) {
        this.league_img_url = league_img_url;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
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

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public TeamInfo getTeam1_info() {
        return team1_info;
    }

    public void setTeam1_info(TeamInfo team1_info) {
        this.team1_info = team1_info;
    }

    public int getTeam1_score() {
        return team1_score;
    }

    public void setTeam1_score(int team1_score) {
        this.team1_score = team1_score;
    }

    public TeamInfo getTeam2_info() {
        return team2_info;
    }

    public void setTeam2_info(TeamInfo team2_info) {
        this.team2_info = team2_info;
    }

    public int getTeam2_score() {
        return team2_score;
    }

    public void setTeam2_score(int team2_score) {
        this.team2_score = team2_score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
