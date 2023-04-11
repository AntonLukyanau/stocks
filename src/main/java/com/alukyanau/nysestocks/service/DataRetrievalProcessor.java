package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.util.RequestParameters;

public interface DataRetrievalProcessor<T> {

    T retrievalProcess(RequestParameters requestParameters);

}
