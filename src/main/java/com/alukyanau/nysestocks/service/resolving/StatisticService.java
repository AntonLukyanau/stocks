package com.alukyanau.nysestocks.service.resolving;

/**
 * Expose methods for calculating statistic about stocks
 *
 * @param <T> Type of calculation result
 */
public interface StatisticService<T> {

    T getStockStatistic(String companyCode, String start, String end);

    T getStockStatistic(String companyCode, Integer month, Integer year);
}
