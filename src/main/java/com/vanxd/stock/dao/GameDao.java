package com.vanxd.stock.dao;

import com.vanxd.stock.vo.Game;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class GameDao extends BaseDao{

    public int buy(String code, Game game) {
        String sql = "INSERT INTO game (code, mount, buy_price, buy_date, hold) VALUES (?, ?,  ?, ?, ?)";
        return jdbcTemplate.update(sql, code, game.getMount(), game.getBuyPrice(), game.getBuyDate(), game.getHold());
    }

    public int sell(Game game, LocalDate sellDate) {
        String sql = "UPDATE game SET mount = ?, hold_day = ? WHERE id = ?";
        return jdbcTemplate.update(sql, game.getMount(), game.getBuyDate().until(sellDate, ChronoUnit.DAYS), game.getId());
    }

    public List<Game> selectNeedSellGame(String code) {
        String sql = "SELECT * FROM game WHERE code = ? AND hold_day = -1";
        return jdbcTemplate.query(sql, new Object[]{code}, BeanPropertyRowMapper.newInstance(Game.class));
    }

    public void flushdb() {
        String sql = "truncate table game";
        jdbcTemplate.update(sql);
    }
}
