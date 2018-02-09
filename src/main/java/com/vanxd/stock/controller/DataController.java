package com.vanxd.stock.controller;

import com.alibaba.fastjson.JSONObject;
import com.vanxd.stock.service.stock.StockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    @Autowired
    private StockDataService stockDataService;

    @GetMapping("/getData.json")
    public JSONObject getData(String code) {
        JSONObject list = stockDataService.list(code);
        return list;
    }

    @GetMapping("/todayChange.json")
    public void todayChange(String code ,String time, Double buyPrice) {
        System.out.println(code);
        System.out.println(time);
        System.out.println(buyPrice);
    }
}
