package com.vanxd.stock;

import org.junit.Test;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

public class SimpleTest {

    @Test
    public void test() {
        LocalDate now = LocalDate.now();
        LocalDate of = LocalDate.of(2018, 1, 1);
        System.out.println(of.until(now));
    }
}
