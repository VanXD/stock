package com.vanxd.stock.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseDao {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected JdbcTemplate jdbcTemplate;
}
