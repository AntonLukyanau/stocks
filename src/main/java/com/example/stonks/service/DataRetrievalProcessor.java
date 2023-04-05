package com.example.stonks.service;

import java.util.Map;

public interface DataRetrievalProcessor<T> {

    T retrievalProcess(Map<String, Object> requestParameters);

}
