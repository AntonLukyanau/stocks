package com.alukyanau.nysestocks.service;

import java.util.Collection;

/**
 * Expose method for aggregation data from items collection
 *
 * @param <T> Aggregated data type
 * @param <D> Source data type
 */
public interface StatisticAggregator<T, D> {

    T aggregate(Collection<D> items);

}
