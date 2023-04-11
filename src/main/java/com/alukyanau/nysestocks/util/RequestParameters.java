package com.alukyanau.nysestocks.util;

import com.alukyanau.nysestocks.dto.NYSEResultFrequency;

import java.time.LocalDate;

public record RequestParameters(
        String companyName,
        NYSEResultFrequency frequency,
        LocalDate startDate,
        LocalDate endDate) {

    public boolean isContainsNull() {
        return companyName == null
                || frequency == null
                || startDate == null
                || endDate == null;
    }

}
