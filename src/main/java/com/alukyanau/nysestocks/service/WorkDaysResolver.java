package com.alukyanau.nysestocks.service;

import java.time.LocalDate;

/**
 * Expose methods for :
 * - resolve last working day
 * - check the date for the fact that it isn't a holiday
 */
public interface WorkDaysResolver {

    LocalDate resolveLastWorkDayBefore(LocalDate date);

    boolean isNonWorkingDay(LocalDate date);

}
