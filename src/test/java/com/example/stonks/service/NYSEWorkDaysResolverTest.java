package com.example.stonks.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NYSEWorkDaysResolverTest {

    private final NYSEWorkDaysResolver workDaysResolver = new NYSEWorkDaysResolver();

    @Test
    void testResolveLastWorkDayBeforeMonday() {
        //given
        LocalDate monday = LocalDate.of(2023, Month.APRIL, 3);
        //when
        LocalDate workDay = workDaysResolver.resolveLastWorkDayBefore(monday);
        //then
        assertEquals(LocalDate.of(2023, Month.MARCH, 31), workDay);
    }

    @Test
    void testResolveLastWorkDayBeforeHoliday() {
        //given
        LocalDate xMas = LocalDate.of(2020, Month.DECEMBER, 25);
        //when
        LocalDate workDay = workDaysResolver.resolveLastWorkDayBefore(xMas);
        //then
        assertEquals(LocalDate.of(2020, Month.DECEMBER, 24), workDay);
    }

    @Test
    void isNonWorkingDayOnHolidays() {
        //given
        LocalDate xMas = LocalDate.of(2023, Month.DECEMBER, 25);
        LocalDate newYear = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate thirdMondayOfJanuary = LocalDate.of(2023, Month.JANUARY, 16);
        LocalDate thirdMondayOfFebruary = LocalDate.of(2023, Month.JANUARY, 16);
        LocalDate lastMondayOfMay = LocalDate.of(2023, Month.MAY, 29);
        LocalDate forthOfJuly = LocalDate.of(2023, Month.JULY, 4);
        LocalDate firstMondayOfSeptember = LocalDate.of(2022, Month.SEPTEMBER, 5);
        LocalDate forthThursdayOfNovember = LocalDate.of(2022, Month.NOVEMBER, 24);
        LocalDate sunday = LocalDate.of(2023, Month.APRIL, 2);
        //when
        boolean isNonWorkingDay1 = workDaysResolver.isNonWorkingDay(xMas);
        boolean isNonWorkingDay2 = workDaysResolver.isNonWorkingDay(newYear);
        boolean isNonWorkingDay3 = workDaysResolver.isNonWorkingDay(thirdMondayOfJanuary);
        boolean isNonWorkingDay4 = workDaysResolver.isNonWorkingDay(thirdMondayOfFebruary);
        boolean isNonWorkingDay5 = workDaysResolver.isNonWorkingDay(lastMondayOfMay);
        boolean isNonWorkingDay6 = workDaysResolver.isNonWorkingDay(forthOfJuly);
        boolean isNonWorkingDay7 = workDaysResolver.isNonWorkingDay(firstMondayOfSeptember);
        boolean isNonWorkingDay8 = workDaysResolver.isNonWorkingDay(forthThursdayOfNovember);
        boolean isNonWorkingDay9 = workDaysResolver.isNonWorkingDay(sunday);
        //then
        assertTrue(isNonWorkingDay1);
        assertTrue(isNonWorkingDay2);
        assertTrue(isNonWorkingDay3);
        assertTrue(isNonWorkingDay4);
        assertTrue(isNonWorkingDay5);
        assertTrue(isNonWorkingDay6);
        assertTrue(isNonWorkingDay7);
        assertTrue(isNonWorkingDay8);
        assertTrue(isNonWorkingDay9);
    }
}