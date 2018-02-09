package com.vanxd.stock.service.stock;

import com.alibaba.fastjson.JSONObject;
import com.vanxd.stock.vo.Game;

public interface StockDataService {

    JSONObject list(String code);

    void editToday(String code, Game game);
}
