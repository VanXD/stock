package com.vanxd.stock.dao;

import com.vanxd.stock.vo.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class TodayChanceDao extends BaseDao{

    public void edit(String code, Double buyPrice) {
        if (buyPrice < 4) {
            return;
        }
        if (update(code, buyPrice) < 1) {
            save(code, buyPrice);
        }
    }

    public int save(String code, Double buyPrice) {
        String sql = "INSERT INTO today (code, buy_price, today) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, code, buyPrice, LocalDate.now());
    }

    public int update(String code, Double buyPrice) {
        String sql = "UPDATE today SET code=?, buy_price=?, today=? WHERE code = ?";
        return jdbcTemplate.update(sql, code, buyPrice, LocalDate.now(), code);
    }
}
