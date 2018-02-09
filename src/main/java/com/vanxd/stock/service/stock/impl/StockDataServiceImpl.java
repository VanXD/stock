package com.vanxd.stock.service.stock.impl;

import com.alibaba.fastjson.JSONObject;
import com.vanxd.stock.dao.GameDao;
import com.vanxd.stock.dao.TodayChanceDao;
import com.vanxd.stock.service.stock.StockDataService;
import com.vanxd.stock.vo.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StockDataServiceImpl implements StockDataService {
    private final static String urlFmt = "https://xueqiu.com/stock/forchartk/stocklist.json?symbol=%s&period=1day&type=before&begin=1483228800000";
    @Autowired
    private TodayChanceDao todayChanceDao;
    @Autowired
    private WebClient webClient;

    @Cacheable(value = "stock", key = "#code")
    @Override
    public JSONObject list(String code) {
        String url = String.format(urlFmt, code);
        JSONObject result = request(url);
        return result;
    }

    @Override
    public void editToday(String code, Game game) {
        todayChanceDao.edit(code, game.getBuyPrice());
    }

    private JSONObject request(String url) {
        Mono<JSONObject> jsonObjectMono = webClient.get()
                .uri(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .header("Cookie", "s=f414o2ymiw; device_id=45b8c48289bab2e6b5b1aa6c74fd535c; __utmz=1.1509507106.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=1.2130251845.1509507106.1509507106.1516604970.2; Hm_lvt_1db88642e346389874251b5a1eded6e3=1516604970,1517209983; xq_a_token=b9872b3ff43406e945d8afe8cf86d5348b935fde; xqat=b9872b3ff43406e945d8afe8cf86d5348b935fde; xq_r_token=678cdf90469d920bdaab0eb6f970d9c62aba7513; xq_token_expire=Fri%20Feb%2023%202018%2015%3A13%3A20%20GMT%2B0800%20(CST); xq_is_login=1; u=7267478675; bid=7eafdcf1149adcf6913debeb3745009b_jczvnsyj; aliyungf_tc=AQAAADEqkRil0gYAIh6s3P9G5ct2apeB")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JSONObject.class);
        try {
            return jsonObjectMono.block();
        } catch (Exception e) {
            return null;
        }

    }
}
