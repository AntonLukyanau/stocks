package com.alukyanau.nysestocks.service.csv;

import com.alukyanau.nysestocks.model.RequestParameters;

/**
 * Implementations should retrieve data by RequestParameters
 * @see RequestParameters
 * @param <T> Type of retrieved data
 */
public interface DataRetriever<T> {

    T retrieveData(RequestParameters parameters);

}
