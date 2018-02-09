CODE_RECORD = [];

(() => {
    let dataPromise = requestData(),
        gap = 4;
    const hit = [],
        leastHit = 1,
        maxHit = 6;
    return;
    for (let promise of dataPromise) {
        promise.then(
            data => gameSandbox(data),
            error => console.error(error)
        );
    }

    function gameSandbox(stock) {
        let data = stock.chartlist;
        console.log("start, %o", stock.stock.symbol);
        console.log(data);
        let game = {
            mount: 10000,
            hold: 0,
            buyPrice: 0,
            buyPos: 0
        };
        for (let i = gap + 1; i < data.length; i++) {
            let avgHighLow = getAvgHighLow(data[i]);
            if (isCycle(data, avgHighLow, i, i - 1, true)) {
                // console.log("OK, %o", data[i]);
                hit.push(new Date(data[i].time).getTime());
                if (!isSatisified(data[i])) {
                    continue;
                }
                let buyCount = parseInt(game.mount / avgHighLow);
                if (buyCount > 0) {
                    game.hold += buyCount;
                    game.mount -= buyCount * avgHighLow;
                    game.buyPrice = avgHighLow;
                    game.buyPos = i;
                    console.log("buy: %o, %O, %s", game, data[i], data[i].time);
                    if (new Date(data[i].time).format("yyyy-MM-dd") === new Date(new Date().getTime() - 86400000).format("yyyy-MM-dd")) {
                        console.warn("BUY: %o, %O, %s", game, data[i], data[i].time);
                        $.ajax({
                            url: "/todayChange.json",
                            data: {
                                code: stock.stock.symbol,
                                time: new Date(data[i].time).format("yyyy-MM-dd"),
                                buyPrice: avgHighLow
                            }
                        })
                    }
                }
            }
            let sellPrice = (game.buyPrice + (game.buyPrice * 0.08));
            if (game.hold > 0 && (sellPrice <= data[i].high)) {
                game.mount += game.hold * avgHighLow;
                game.buyPrice = 0;
                game.hold = 0;
                console.log("sell: %d, %o,%s", i - game.buyPos, game, data[i].time);
            }
        }
        console.log(game);
    }

    function isSatisified(day) {
        let arr = hit.filter(item => item >= (new Date(day.time).getTime() - 7776000000));
        return arr.length > leastHit && arr.length < maxHit;
    }

    function isCycle(data, targetValue, i, dynamicPos, isValid) {
        if (!isValid) {
            return false;
        }
        let avgHighLow = getAvgHighLow(data[dynamicPos]);
        if (dynamicPos + gap + 1 === i) {
            return ((avgHighLow >= targetValue - targetValue * 0.03) && (avgHighLow <= targetValue + targetValue * 0.03)) && isValid
        }
        let tmpResult = isCycle(data, targetValue, i, dynamicPos - 1, avgHighLow > targetValue);
        return tmpResult;
    }

    function getAvgHighLow(day) {
        if (!day) {
            return 9999;
        }
        return (day.high + day.low) / 2;
    }
})();

function requestData() {
    let dataPromiseArray = [];
        // codeArray = [1,2,7,8,5,6,9,4,10,11,12,14,16,17,18,19,20,21,24,22,25,23,31,26,28,32,27,35,29,34,33,30,39,37,42,38,43,36,46,48,49,40,50,55,56,45,58,59,65,61,63,66,68,60,70,62,69,78,89,88,90,99,96,100,153,151,150,157,156,155,158,159,166,301,333,338,401,400,404,402,403,408,413,409,407,416,411,419,417,410,421,420,415,418,422,426,430,425,423,429,428,488,498,503,501,506,509,502,510,504,507,505,511,516,514,519,517,518,513,520,522,521,525,524,528,527,531,529,530,523,534,532,533,537,526,538,539,541,540,545,543,544,547,536,546,550,548,553,554,552,555,559,557,558,561,562,560,565,564,568,567,570,566,572,576,551,582,581,584,585,586,587,571,589,590,563,592,593,596,595,594,597,601,598,602,573,600,603,607,599,606,609,608,591,612,611,615,613,617,616,610,619,605,620,623,627,622,626,630,631,633,632,635,625,628,637,629,638,636,651,639,650,655,656,652,659,661,657,666,668,662,667,663,670,669,672,665,676,673,677,680,679,682,678,683,685,671,686,688,681,691,690,695,687,697,693,701,703,700,698,702,707,705,708,710,709,692,713,711,716,712,719,721,718,723,720,725,728,729,727,722,715,733,735,726,736,738,732,731,739,717,737,752,755,748,756,758,753,751,759,762,757,750,767,760,766,768,761,776,780,783,779,778,785,788,789,782,790,787,792,786,795,791,797,777,800,801,799,802,803,806,807,810,809,796,793,811,812,816,819,818,822,813,820,798,823,828,821,815,830,833,826,835,837,825,839,836,829,838,831,848,851,856,859,860,850,861,863,858,869,852,862,868,875,876,881,877,880,886,878,887,888,890,885,892,882,889,893,895,900,898,883,901,897,903,908,905,902,910,899,909,911,913,912,915,917,906,916,918,920,921,919,925,926,923,929,922,930,927,932,931,928,936,935,937,933,938,939,950,948,953,955,951,957,949,959,958,961,963,952,960,965,966,969,962,968,972,971,970,967,976,975,979,981,973,982,977,978,983,985,980,989,990,987,995,993,988,997,999,996,998];
        // codeArray = [537];
    for (let i = 2000; i < 10000; i++) {
        let promiseTmp = new Promise((resolve, reject) => {
            $.ajax({
                url: "/getData.json",
                data: {
                    code: "SZ" + "000000".substr(("" + i).length) + i
                },
                success: res => {
                    if (res.error_code) {
                        reject(res);
                    } else {
                        CODE_RECORD.push(i);
                        resolve(res)
                    }
                },
                error: res => {
                    console.log(res);
                }
            });
        });
        dataPromiseArray.push(promiseTmp);
    }
    return dataPromiseArray;
}