package com.example.stonks.service;

import com.example.stonks.util.RequestParameters;

public interface DataRetrievalProcessor<T> {

    T retrievalProcess(RequestParameters requestParameters);

}
