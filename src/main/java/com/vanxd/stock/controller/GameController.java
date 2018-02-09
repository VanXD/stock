package com.vanxd.stock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vanxd.stock.dao.GameDao;
import com.vanxd.stock.global.Const;
import com.vanxd.stock.service.stock.GameService;
import com.vanxd.stock.service.stock.StockDataService;
import com.vanxd.stock.vo.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlElementDecl;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class GameController {
    Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private StockDataService stockDataService;
    @Resource(name = "gameCircleMa5UnserMa202ServiceImpl")
    private GameService gameService;
    @Autowired
    private GameDao gameDao;
    private final static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(8, 16, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());

    @GetMapping("/game.json")
    public void game() {
        gameDao.flushdb();

        for (int i = 0; i < Const.GLOBAL_CODE.length; i++) {
            int finalI = i;
            THREAD_POOL.submit(() -> {
                String code = Const.GLOBAL_CODE[finalI];
                logger.info("开始：{}", code);
                JSONObject result = stockDataService.list(code);
                if (null == result) {
                    return;
                }
                List<Day> data = JSON.parseArray(JSON.toJSONString(result.get("chartlist")), Day.class);
                gameService.compute(code, data);
            });
        }
    }

    @GetMapping("/gameTest.json")
    public void gameTest() {
//        String[] testCode = {"SZ000526", "SH600456", "SZ002812", "SH603960", "SZ002333"};
        String[] testCode = {"SZ002235"};
        gameDao.flushdb();
        for (int i = 0; i < testCode.length; i++) {
            int finalI = i;
            THREAD_POOL.submit(() -> {
                String code = testCode[finalI];
                logger.info("开始：{}", code);
                JSONObject result = stockDataService.list(code);
                if (null == result) {
                    return;
                }
                List<Day> data = JSON.parseArray(JSON.toJSONString(result.get("chartlist")), Day.class);
                gameService.compute(code, data);
            });
        }
    }
}
