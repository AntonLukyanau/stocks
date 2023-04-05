package com.example.stonks.service;

import java.time.LocalDate;

public interface WorkDaysResolver {

    LocalDate resolveLastWorkDayBefore(LocalDate date);

    boolean isNonWorkingDay(LocalDate date);

}
