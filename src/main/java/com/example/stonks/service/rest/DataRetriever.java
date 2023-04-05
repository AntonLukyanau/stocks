package com.example.stonks.service.rest;

import java.util.Map;

public interface DataRetriever<T> {

    T retrieveData(Map<String, Object> parameters);

}
