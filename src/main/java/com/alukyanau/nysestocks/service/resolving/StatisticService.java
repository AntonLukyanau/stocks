package com.alukyanau.nysestocks.service.resolving;

public interface StatisticService<T> {

    T getStockStatistic(String companyCode, String start, String end);

    T getStockStatistic(String companyCode, Integer month, Integer year);
}
