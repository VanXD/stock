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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 53.3333
 */
@Service
public class GameCircleMa5UnserMa20ServiceImpl implements GameService {
    private Logger LOGGER = LoggerFactory.getLogger(GameCircleMa5UnserMa20ServiceImpl.class);
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
        List<Day> buyRecords = new ArrayList<>();
        for (int i = 30; i < data.size(); i++) {
            if (needRefreshSellGameList) {
                needSellGameList = gameDao.selectNeedSellGame(code);
                needRefreshSellGameList = false;
            }
            Day day = data.get(i);
            day.setDate(DateUtil.timestampToLocalDate(day.getTimestamp()));
            double avgHighLow = getAvgHighLow(day);
            if (isMa5UnderMa20(data, i, buyRecords)) {
                buyRecords.add(day);
                if (getTodayTimestamp() == day.getTimestamp()) {
                    todayChanceDao.edit(code, avgHighLow);
                }
                int buyCount = (int) (new Game().getMount() / avgHighLow);
                needRefreshSellGameList = saveGameBuy(code, avgHighLow, buyCount, day);
            }
            updateGameSell(day, avgHighLow, needSellGameList);
        }
    }

    private boolean isMa5UnderMa20(List<Day> data, int i, List<Day> buyRecords) {
        Day today = data.get(i);
        Day yesterday = data.get(i - 1);
        if (!CollectionUtils.isEmpty(buyRecords)
                && buyRecords.get(buyRecords.size() - 1).getDate().until(today.getDate(), ChronoUnit.DAYS) < 5) {
            return false;
        }

        double abs520 = Math.abs(today.getMa5() - today.getMa20());
        boolean isMaPass = today.getMa5() < today.getMa20() && abs520 > today.getMa5() * 0.1;
        boolean isMaDirectionPass = today.getMa5() > yesterday.getMa5() && today.getMa20() > yesterday.getMa20();
        return isMaPass && isMaDirectionPass;
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
