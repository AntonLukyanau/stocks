package com.alukyanau.nysestocks.model;

import java.time.LocalDate;

public record RequestParameters(
        String companyName,
        LocalDate startDate,
        LocalDate endDate) {

    public boolean isContainsNull() {
        return companyName == null
                || startDate == null
                || endDate == null;
    }

}
