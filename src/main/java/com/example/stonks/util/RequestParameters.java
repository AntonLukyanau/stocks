package com.example.stonks.util;

import com.example.stonks.dto.NYSEResultFrequency;

import java.time.LocalDate;

public record RequestParameters(
        String companyName,
        NYSEResultFrequency frequency,
        LocalDate startDate,
        LocalDate endDate) {
}
