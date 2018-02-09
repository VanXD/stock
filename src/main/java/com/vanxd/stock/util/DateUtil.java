package com.vanxd.stock.util;

import java.time.*;

public class DateUtil {

    public static LocalDate timestampToLocalDate(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.of("+8"));
        return offsetDateTime.toLocalDate();
    }

    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.of("+8"));
        return offsetDateTime.toLocalDateTime();
    }
}
