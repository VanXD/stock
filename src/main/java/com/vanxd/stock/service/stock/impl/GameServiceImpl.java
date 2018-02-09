package com.vanxd.stock.service.stock.impl;

import com.vanxd.stock.dao.GameDao;
import com.vanxd.stock.service.stock.GameService;
import com.vanxd.stock.service.stock.StockDataService;
import com.vanxd.stock.util.DateUtil;
import com.vanxd.stock.vo.Day;
import com.vanxd.stock.vo.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);
    private final static int GAP = 4;
    private final static int LEAST_HIT = 1;
    private final static int MAX_HIT = 6;
    @Autowired
    private StockDataService stockDataService;

    @Override
    public void compute(String code, List<Day> data) {
        Game game = new Game();
        List<Long> hit = game.getHit();
        for (int i = GAP + 1; i < data.size(); i++) {
            Day day = data.get(i);
            day.setDate(DateUtil.timestampToLocalDate(day.getTimestamp()));
            double avgHighLow = getAvgHighLow(day);
            if (isCycle(data, avgHighLow, i, i - 1, true)) {
                LOGGER.info("OK: {}, {},{}", code, game, day);
                hit.add(day.getTimestamp());
                if (!isSatisified(day, hit)) {
                    continue;
                }
                if (getTodayTimestamp() == day.getTimestamp()) {
                    LOGGER.error("TODAY: {}, {}, {}", code, game, day);
                    stockDataService.editToday(code, game);
                }
                int buyCount = (int) (game.getMount() / avgHighLow);
                if (buyCount > 0) {
                    game.setHold(game.getHold() + buyCount);
                    game.setMount(game.getMount() - buyCount * avgHighLow);
                    game.setBuyPrice(avgHighLow);
                    game.setBuyDate(day.getDate());
                    LOGGER.info("buy: {}, {}, {}", code, game, day);
                }
            }
            double sellPrice = (game.getBuyPrice() + (game.getBuyPrice() * 0.08));
            if (game.getHold() > 0 && (sellPrice <= day.getHigh())) {
                game.setMount(game.getHold() * avgHighLow);
                game.setBuyPrice(0D);
                game.setHold(0);
                LOGGER.warn("sell: {}, {}, {}, {}", code, game.getBuyDate().until(day.getDate()), game, day);
            }
        }
    }

    private long getTodayTimestamp() {
        LocalDateTime now = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    private boolean isSatisified(Day day, List<Long> hit) {
        List<Long> result = hit.stream().filter(item -> item >= (day.getTimestamp() - 7776000000L)).collect(Collectors.toList());
        return result.size() > LEAST_HIT && result.size() < MAX_HIT;

    }

    private boolean isCycle(List<Day> data, Double targetValue, int i, int dynamicPos, boolean isValid) {
        if (!isValid) {
            return false;
        }
        double avgHighLow = getAvgHighLow(data.get(dynamicPos));
        if (dynamicPos + GAP + 1 == i) {
            double bottom = targetValue - targetValue * 0.03;
            double top = targetValue + targetValue * 0.03;
            return ((avgHighLow >= bottom) && (avgHighLow <= top)) && isValid;
        }
        boolean tmpResult = isCycle(data, targetValue, i, dynamicPos - 1, avgHighLow > targetValue);
        return tmpResult;
    }

    private double getAvgHighLow(Day day) {
        if (null == day) {
            return 9999;
        }
        return (day.getHigh() + day.getLow()) / 2;
    }
}
