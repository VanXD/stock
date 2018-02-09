package com.vanxd.stock.controller;

import com.vanxd.stock.dao.MaxplusMatchDao;
import com.vanxd.stock.entity.MaxplusMatch;
import com.vanxd.stock.util.DateUtil;
import com.vanxd.stock.vo.maxplus.Match;
import com.vanxd.stock.vo.maxplus.MaxCategory;
import com.vanxd.stock.vo.maxplus.MaxResult;
import com.vanxd.stock.vo.maxplus.TeamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MaxPlus2Controller {
    Logger logger = LoggerFactory.getLogger(MaxPlus2Controller.class);
    @Autowired
    private WebClient webClient;
    @Autowired
    private MaxplusMatchDao maxplusMatchDao;
    private final static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(8, 16, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
    private final static String API = "https://q.maxjia.com/api/bets/get_all_category/3/?&bet_type=all&limit=30&offset=%d";
    private static AtomicInteger offsetStart = new AtomicInteger(0);
    private final static int GAP = 30;


    @GetMapping("/v2/saveMaxPlusData.json")
    public void saveData() {
        offsetStart.set(0);
        for (int i = 0; i < 1; i++) {
            THREAD_POOL.submit(() -> {
                while (true) {
//                    String url = String.format(API, offsetStart.getAndAdd(GAP));
                    String url = "https://q.maxjia.com/api/bets/get_all_category/3/?&bet_type=all&limit=30&offset=99999";
                    logger.info("准备请求：{}", url);
                    MaxResult maxResult;
                    try {
                        maxResult = request(url);
                    } catch (Exception e) {
                        logger.error("请求失败，恢复offset：", e);
                        offsetStart.addAndGet(-GAP);
                        continue;
                    }
                    Mono.just(maxResult)
                            .map(MaxResult::getResult)
                            .map(maxCategories -> {
                                if (CollectionUtils.isEmpty(maxCategories)) {
                                    throw new RuntimeException("没有数据了");
                                }
                                return Flux.just(maxCategories);
                            })
                            .subscribe(what -> {
                                System.out.println(what);
                            });

//
//                    List<MaxCategory> result = maxResult.getResult();
//                    if (CollectionUtils.isEmpty(result)) {
//                        logger.info("结束");
//                        return;
//                    }
//                    for (MaxCategory maxCategory : result) {
//                        List<Match> matches = maxCategory.getMatches();
//                        for (Match match : matches) {
//                            if (!"已结算".equals(match.getProgress_desc())) {
//                                continue;
//                            }
//                            MaxplusMatch maxplusMatch = buildMaxplusMatch(maxCategory, match);
//                            maxplusMatchDao.insert(maxplusMatch);
//                        }
//                    }
                }

            });
        }
    }

    private MaxplusMatch buildMaxplusMatch(MaxCategory maxCategory, Match match) {
        TeamInfo team1Info = maxCategory.getTeam1_info();
        TeamInfo team2Info = maxCategory.getTeam2_info();
        MaxplusMatch maxplusMatch = new MaxplusMatch();
        maxplusMatch.setCategory_id(maxCategory.getCategory_id());
        maxplusMatch.setMatch_id(match.getMatch_id());
        maxplusMatch.setTitle(maxCategory.getTitle());
        maxplusMatch.setSub_title(match.getSub_title());
        maxplusMatch.setProgress_desc(match.getProgress_desc());
        maxplusMatch.setEnd_bid_time(DateUtil.timestampToLocalDateTime(Long.parseLong(match.getEnd_bid_time().substring(0, match.getEnd_bid_time().indexOf("."))) * 1000));
        if (team1Info.getTeam_id() == match.getResult()) {
            maxplusMatch.setWin_team_name(team1Info.getTag());
            maxplusMatch.setWin_team_score(maxCategory.getTeam1_score());
            maxplusMatch.setLose_team_name(team2Info.getTag());
            maxplusMatch.setLose_team_score(maxCategory.getTeam2_score());
        } else {
            maxplusMatch.setWin_team_name(team2Info.getTag());
            maxplusMatch.setWin_team_score(maxCategory.getTeam2_score());
            maxplusMatch.setLose_team_name(team1Info.getTag());
            maxplusMatch.setLose_team_score(maxCategory.getTeam1_score());
        }
        return maxplusMatch;
    }

    private MaxResult request(String url) {
        Mono<MaxResult> jsonObjectMono = webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MaxResult.class);
        return jsonObjectMono.block();
    }
}