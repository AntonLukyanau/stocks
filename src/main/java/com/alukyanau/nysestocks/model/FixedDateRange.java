package com.alukyanau.nysestocks.model;

import java.time.LocalDate;

/**
 * Represent range of fixed dates
 */
public record FixedDateRange(LocalDate start, LocalDate end) {
}
