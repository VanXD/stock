package com.vanxd.stock.dao;

import com.vanxd.stock.entity.MaxplusMatch;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public class MaxplusMatchDao extends BaseDao {

    public int insert(MaxplusMatch maxplusMatch) {
        String sql = "INSERT INTO maxplus_match (progress_desc, title, sub_title," +
                " end_bid_time, win_team_name, win_team_score, lose_team_name," +
                " lose_team_score, match_id, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, maxplusMatch.getProgress_desc(),
                                        maxplusMatch.getTitle(),
                                        maxplusMatch.getSub_title(),
                                        maxplusMatch.getEnd_bid_time(),
                                        maxplusMatch.getWin_team_name(),
                                        maxplusMatch.getWin_team_score(),
                                        maxplusMatch.getLose_team_name(),
                                        maxplusMatch.getLose_team_score(),
                                        maxplusMatch.getMatch_id(),
                                        maxplusMatch.getCategory_id());
    }
}
