package com.alukyanau.nysestocks.service.retrieving;

import com.alukyanau.nysestocks.model.RequestParameters;

/**
 * Provide method for do retrieving process
 * @param <T> Type of method return
 */
public interface DataRetrievalProcessor<T> {

    T retrievalProcess(RequestParameters requestParameters);

}
