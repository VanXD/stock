package com.vanxd.stock.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateUtil {

    public static LocalDate timestampToLocalDate(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.of("+8"));
        return offsetDateTime.toLocalDate();
    }
}
