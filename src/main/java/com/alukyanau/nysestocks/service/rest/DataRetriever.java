package com.alukyanau.nysestocks.service.rest;

import com.alukyanau.nysestocks.util.RequestParameters;

public interface DataRetriever<T> {

    T retrieveData(RequestParameters parameters);

}
