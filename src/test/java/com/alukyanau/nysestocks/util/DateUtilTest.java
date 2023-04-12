package com.alukyanau.nysestocks.util;

import com.alukyanau.nysestocks.model.FixedDateRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {

    private final DateUtil dateUtil = new DateUtil();

    @Test
    void testCalculateDaysPeriod() {
        // given
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 10);
        // when
        int calculatedPositive = dateUtil.calculateDaysPeriod(start, end);
        int calculatedNegative = dateUtil.calculateDaysPeriod(end, start);
        // then
        assertEquals(9, calculatedPositive);
        assertEquals(9, calculatedNegative);
    }

    @Test
    void testResolveOneMonthFixedPeriodWithUsualMonth() {
        // given
        Integer month = 1;
        Integer year = 2023;
        // when
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        // then
        assertEquals(LocalDate.of(2023, 1, 1), fixedDateRange.start());
        assertEquals(LocalDate.of(2023, 1, 31), fixedDateRange.end());
    }

    @Test
    void testResolveOneMonthFixedPeriodWithFebruaryInLeapYear() {
        // given
        Integer month = 2;
        Integer year = 2024;
        // when
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        // then
        assertEquals(LocalDate.of(2024, 2, 1), fixedDateRange.start());
        assertEquals(LocalDate.of(2024, 2, 29), fixedDateRange.end());
    }

    @Test
    void testResolveOneMonthFixedPeriodWithFebruaryInNonLeapYear() {
        // given
        Integer month = 2;
        Integer year = 2023;
        // when
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        // then
        assertEquals(LocalDate.of(2023, 2, 1), fixedDateRange.start());
        assertEquals(LocalDate.of(2023, 2, 28), fixedDateRange.end());
    }

    @Test
    void testResolveOneMonthFixedPeriodWithNullParameters() {
        // given
        Integer month = null;
        Integer year = null;
        LocalDate today = LocalDate.now();
        // when
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        // then
        assertEquals(today.withDayOfMonth(1), fixedDateRange.start());
        assertEquals(today.withDayOfMonth(today.lengthOfMonth()), fixedDateRange.end());
    }

    @Test
    void testResolveOneMonthFixedPeriodWithUnboundedParameters() {
        // given
        Integer month = 13;
        Integer year = 1791; // NYSE was opened in 1792
        LocalDate today = LocalDate.now();
        // when
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        // then
        assertEquals(today.withDayOfMonth(1), fixedDateRange.start());
        assertEquals(today.withDayOfMonth(today.lengthOfMonth()), fixedDateRange.end());
    }
}