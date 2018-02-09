package com.vanxd.stock.controller;

import com.vanxd.stock.dao.MaxplusMatchDao;
import com.vanxd.stock.entity.MaxplusMatch;
import com.vanxd.stock.util.DateUtil;
import com.vanxd.stock.vo.maxplus.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MaxPlusCsgoController {
    Logger logger = LoggerFactory.getLogger(MaxPlusCsgoController.class);
    @Autowired
    private WebClient webClient;
    @Autowired
    private MaxplusMatchDao maxplusMatchDao;
    private final static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(8, 16, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
    private final static String API = "http://api.maxjia.com/api/csgo/league/match_list/v2/?limit=30&offset=%d";
    private static AtomicInteger offsetStart = new AtomicInteger(0);
    private final static int GAP = 30;


    @GetMapping("/saveMaxPlusCsgoData.json")
    public void saveData() {
        offsetStart.set(0);
        for (int i = 0 ;i < 8;i++) {
            THREAD_POOL.submit(() -> {
                while (true) {
                    String url = String.format(API, offsetStart.getAndAdd(GAP));
                    logger.info("准备请求：{}", url);
                    CsgoResult maxResult;
                    try {
                        maxResult = request(url);
                    } catch (Exception e) {
                        logger.error("请求失败，恢复offset：", e);
                        offsetStart.addAndGet(-GAP);
                        continue;
                    }
                    List<CsgoEvent> result = maxResult.getResult();
                    if (CollectionUtils.isEmpty(result)) {
                        logger.info("结束");
                        return;
                    }
                    for (CsgoEvent csgoEvent : result) {
                        if (!"Finished".equals(csgoEvent.getState())) {
                            break;
                        }
                        List<ScoreRound> matches = csgoEvent.getScore_round();
                        for (ScoreRound round : matches) {
                            MaxplusMatch maxplusMatch = buildMaxplusMatch(csgoEvent, round);
                            maxplusMatchDao.insert(maxplusMatch);
                        }
                    }
                }

            });
        }
    }

    private MaxplusMatch buildMaxplusMatch(CsgoEvent csgoEvent, ScoreRound round) {
        MaxplusMatch maxplusMatch = new MaxplusMatch();
        maxplusMatch.setCategory_id(csgoEvent.getEvent_id());
        maxplusMatch.setMatch_id(null);
        maxplusMatch.setTitle(csgoEvent.getBo());
        maxplusMatch.setSub_title(csgoEvent.getBo());
        maxplusMatch.setProgress_desc(csgoEvent.getState());
        maxplusMatch.setEnd_bid_time(DateUtil.timestampToLocalDateTime(Long.valueOf(csgoEvent.getEven_time() * 1000)));
        if (round.getTeam1_score() > round.getTeam2_score()) {
            maxplusMatch.setWin_team_name(csgoEvent.getTeam1_name());
            maxplusMatch.setWin_team_score(round.getTeam1_score());
            maxplusMatch.setLose_team_name(csgoEvent.getTeam2_name());
            maxplusMatch.setLose_team_score(round.getTeam2_score());
        } else {
            maxplusMatch.setWin_team_name(csgoEvent.getTeam2_name());
            maxplusMatch.setWin_team_score(round.getTeam2_score());
            maxplusMatch.setLose_team_name(csgoEvent.getTeam1_name());
            maxplusMatch.setLose_team_score(round.getTeam1_score());
        }
        return maxplusMatch;
    }

    private CsgoResult request(String url) {
        Mono<CsgoResult> jsonObjectMono = webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CsgoResult.class);
        return jsonObjectMono.block();
    }
}