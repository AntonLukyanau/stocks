package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.model.RequestParameters;

public interface DataRetrievalProcessor<T> {

    T retrievalProcess(RequestParameters requestParameters);

}
