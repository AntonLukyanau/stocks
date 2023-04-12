package com.alukyanau.nysestocks.util;

import com.alukyanau.nysestocks.infrastructure.NYSEConstants;
import com.alukyanau.nysestocks.model.FixedDateRange;

import java.time.LocalDate;
import java.time.Period;

@UtilComponent
public class DateUtil {

    public int calculateDaysPeriod(LocalDate startDate, LocalDate endDate) {
        return Math.abs(
                Period.between(startDate, endDate).getDays()
        );
    }

    public FixedDateRange resolveOneMonthFixedPeriod(Integer month, Integer year) {
        if (month == null || month < 1 || month > 12) {
            month = LocalDate.now().getMonthValue();
        }
        if (year == null || year < NYSEConstants.NYSE_FOUNDED_YEAR) {
            year = LocalDate.now().getYear();
        }
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return new FixedDateRange(startDate, endDate);
    }

}
