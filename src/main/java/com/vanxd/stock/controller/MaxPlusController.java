package com.vanxd.stock.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class MaxPlusController {
    private final static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(8, 16, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
    private final static String API = "https://q.maxjia.com/api/bets/get_all_category/3/?&bet_type=all&limit=30&offset=0";



}
