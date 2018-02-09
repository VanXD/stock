package com.vanxd.stock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vanxd.stock.app.StockApplication;
import com.vanxd.stock.dao.TodayChanceDao;
import com.vanxd.stock.service.stock.GameService;
import com.vanxd.stock.service.stock.StockDataService;
import com.vanxd.stock.vo.Day;
import com.vanxd.stock.vo.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockApplication.class)
public class StockApplicationTests {
	@Autowired
	private TodayChanceDao todayChanceDao;
	@Autowired
	private WebClient webClient;


	@Test
	public void contextLoads() {
	}

	@Test
	public void test() {
		Mono<JSONObject> jsonObjectMono = webClient.get()
				.uri("https://xueqiu.com/stock/forchartk/stocklist.json?symbol=SZ000537&period=1day&type=normal&begin=1483228800000")
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
				.header("Cookie", "s=f414o2ymiw; device_id=45b8c48289bab2e6b5b1aa6c74fd535c; __utmz=1.1509507106.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=1.2130251845.1509507106.1509507106.1516604970.2; Hm_lvt_1db88642e346389874251b5a1eded6e3=1516604970,1517209983; xq_a_token=b9872b3ff43406e945d8afe8cf86d5348b935fde; xqat=b9872b3ff43406e945d8afe8cf86d5348b935fde; xq_r_token=678cdf90469d920bdaab0eb6f970d9c62aba7513; xq_token_expire=Fri%20Feb%2023%202018%2015%3A13%3A20%20GMT%2B0800%20(CST); xq_is_login=1; u=7267478675; bid=7eafdcf1149adcf6913debeb3745009b_jczvnsyj; aliyungf_tc=AQAAADEqkRil0gYAIh6s3P9G5ct2apeB")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(JSONObject.class);
		JSONObject block = jsonObjectMono.block();
		System.out.println(block);
	}

}
