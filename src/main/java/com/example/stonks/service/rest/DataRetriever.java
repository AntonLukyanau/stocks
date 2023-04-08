package com.example.stonks.service.rest;

import com.example.stonks.util.RequestParameters;

public interface DataRetriever<T> {

    T retrieveData(RequestParameters parameters);

}
