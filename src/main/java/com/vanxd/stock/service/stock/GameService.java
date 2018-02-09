package com.vanxd.stock.service.stock;

import com.vanxd.stock.dao.GameDao;
import com.vanxd.stock.vo.Day;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface GameService {
    void compute(String code, List<Day> data);
}
