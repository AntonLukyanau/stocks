package com.alukyanau.nysestocks.service;

import java.util.Collection;

/**
 * Service provide resolving/calculating max and min value of items characteristics
 *
 * @param <T> Aggregated data type
 * @param <D> Source data type
 */
public interface StatisticService<T, D> {

    T aggregate(Collection<D> items);

}
