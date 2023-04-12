package com.alukyanau.nysestocks.service.csv;

import com.alukyanau.nysestocks.model.RequestParameters;

public interface DataRetriever<T> {

    T retrieveData(RequestParameters parameters);

}
