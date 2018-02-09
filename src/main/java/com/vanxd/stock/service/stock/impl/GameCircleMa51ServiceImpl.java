package com.vanxd.stock.service.stock.impl;

import com.vanxd.stock.dao.GameDao;
import com.vanxd.stock.dao.TodayChanceDao;
import com.vanxd.stock.service.stock.GameService;
import com.vanxd.stock.util.DateUtil;
import com.vanxd.stock.vo.Day;
import com.vanxd.stock.vo.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 24.6377
 */
@Service
public class GameCircleMa51ServiceImpl implements GameService {
    private Logger LOGGER = LoggerFactory.getLogger(GameCircleMa51ServiceImpl.class);
    private final static int LEAST_HIT = 1;
    private final static int MAX_HIT = 3;
    private final static int PRICE_TOO_HIGH_CHECK = 25;
    @Autowired
    private TodayChanceDao todayChanceDao;
    @Autowired
    private GameDao gameDao;

    @Override
    public void compute(String code, List<Day> data) {
        List<Game> needSellGameList;
        boolean needRefreshSellGameList = false;
        needSellGameList = gameDao.selectNeedSellGame(code);
        Game game = new Game();
        List<Day> hit = new ArrayList<>();
        for (int i = 30; i < data.size(); i++) {
            if (needRefreshSellGameList) {
                needSellGameList = gameDao.selectNeedSellGame(code);
                needRefreshSellGameList = false;
            }
            Day day = data.get(i);
            day.setDate(DateUtil.timestampToLocalDate(day.getTimestamp()));
            double avgHighLow = getAvgHighLow(day);
            if (isCycle(data, i)) {
                LOGGER.info("OK: {}, {}", code, day);
                boolean isJump = false;
                hit.add(day);
                if (isTooHigh(data, i, avgHighLow)) {
                    LOGGER.warn("价格过高");
                    isJump = true;
                }
                if (!isJump && !isMa20Rise(data, i)) {
                    LOGGER.warn("ma20：不满足");
                    isJump = true;
                }
                if (!isJump && !isHitSatisfied(data, i, hit)) {
                    LOGGER.warn("连击：不满足");
                    isJump = true;
                }
                if (!isJump) {
                    if (getTodayTimestamp() == day.getTimestamp()) {
                        todayChanceDao.edit(code, avgHighLow);
                    }
                    int buyCount = (int) (game.getMount() / avgHighLow);
                    needRefreshSellGameList = saveGameBuy(code, avgHighLow, buyCount, day);
                }
            }
            updateGameSell(day, avgHighLow, needSellGameList);
        }
    }

    private boolean isTooHigh(List<Day> data, int index, double avgHighLow) {
        double historyPrice = getAvgHighLow(data.get(index - PRICE_TOO_HIGH_CHECK));

        return historyPrice + historyPrice * 0.8 < avgHighLow;
    }

    private boolean isMa20Rise(List<Day> data, int i) {
        if (data.get(i).getMa5() < data.get(i).getMa20()) {
            return false;
        }
        return (data.get(i).getMa20() > data.get(i - 2).getMa20()) && (data.get(i).getMa10() > data.get(i - 2).getMa10());
    }

    private void updateGameSell(Day day, double avgHighLow, List<Game> needSellGameList) {
        if (CollectionUtils.isEmpty(needSellGameList)) {
            return;
        }
        Iterator<Game> iterator = needSellGameList.iterator();
        while (iterator.hasNext()) {
            Game soldGame = iterator.next();
            double sellPrice = (soldGame.getBuyPrice() + (soldGame.getBuyPrice() * 0.08));
            if (soldGame.getHold() > 0 && (sellPrice <= day.getHigh())) {
                soldGame.setMount(soldGame.getHold() * avgHighLow);
                soldGame.setHold(0);
                gameDao.sell(soldGame, day.getDate());
                iterator.remove();
            }
        }
    }

    private boolean saveGameBuy(String code, double avgHighLow, int buyCount, Day day) {
        if (avgHighLow < 4) {
            return false;
        }
        Game game = new Game();
        game.setHold(buyCount);
        game.setMount(game.getMount() - buyCount * avgHighLow);
        game.setBuyPrice(day.getClose() + 0.05);
        game.setBuyDate(day.getDate());
        gameDao.buy(code, game);
        return true;
    }

    private long getTodayTimestamp() {
        LocalDateTime now = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    private boolean isHitSatisfied(List<Day> data, int i, List<Day> hit) {
        Day day = data.get(i);
        List<Day> result = hit.stream()
                .filter(item -> item.getTimestamp() >= (day.getTimestamp() - 2592000000L))
                .collect(Collectors.toList());
        List<Day> maxHitResult = hit.stream()
                .filter(item -> item.getTimestamp() >= (day.getTimestamp() - 7776000000L))
                .collect(Collectors.toList());
        Day beforeHit = hit.get(hit.size() - 2);
//        return day.getMa5() > beforeHit.getMa5() ;
        return day.getMa5() > beforeHit.getMa5() && result.size() > LEAST_HIT && maxHitResult.size() < MAX_HIT;

    }

    /**
     * 周期中间的比两边的大
     * @return
     */
    private boolean isCycle(List<Day> data, int i) {
        double left2 = data.get(i - 4).getMa5();
        double left = data.get(i - 3).getMa5();
        double mid = data.get(i - 2).getMa5();
        double right = data.get(i - 1).getMa5();
        double right2 = data.get(i).getMa5();
        // 中间的>两边
        if (mid < left || mid < right) {
            return false;
        }
        // 两边>两边
        if (left < left2 || right < right2) {
            return false;
        }
        return true;
    }

    private double getAvgHighLow(Day day) {
        if (null == day) {
            return 9999;
        }
        return (day.getHigh() + day.getLow()) / 2;
    }
}
