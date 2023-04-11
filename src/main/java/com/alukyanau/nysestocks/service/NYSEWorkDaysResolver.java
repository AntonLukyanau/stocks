package com.alukyanau.nysestocks.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

@Service("nyseWorkDaysResolver")
public class NYSEWorkDaysResolver implements WorkDaysResolver {

    public LocalDate resolveLastWorkDayBefore(LocalDate date) {
        LocalDate workDay = date.minusDays(1);
        while (isNonWorkingDay(workDay)) {
            workDay = workDay.minusDays(1);
        }
        return workDay;
    }

    public boolean isNonWorkingDay(LocalDate date) {
        Set<LocalDate> holidays = getHolidays(date.getYear());
        return date.getDayOfWeek() == DayOfWeek.SUNDAY
                || date.getDayOfWeek() == DayOfWeek.SATURDAY
                || holidays.contains(date);
    }

    private Set<LocalDate> getHolidays(int year) {
        LocalDate firstOfJanuary = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate birthdayOfMartinLutherKingJr = LocalDate.of(year, Month.JANUARY, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
        LocalDate dayOfPresident = LocalDate.of(year, Month.FEBRUARY, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
        LocalDate dayOfMemory = LocalDate.of(year, Month.MAY, 1)
                .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));
        LocalDate dayOfIndependence = LocalDate.of(year, Month.JULY, 4);
        LocalDate dayOfWorking = LocalDate.of(year, Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY));
        LocalDate thanksgivingDay = LocalDate.of(year, Month.NOVEMBER, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY));
        LocalDate christmas = LocalDate.of(year, Month.DECEMBER, 25);

        return Set.of(
                firstOfJanuary, birthdayOfMartinLutherKingJr, dayOfPresident, dayOfMemory,
                dayOfIndependence, dayOfWorking, thanksgivingDay, christmas);
    }

}
