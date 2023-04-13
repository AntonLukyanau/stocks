package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.dto.NormalizedStockData;

import java.util.List;

/**
 * Expose methods for receiving data with calculated normalized value
 */
public interface NormalizeDateService {

    List<NormalizedStockData> getNormalizedStockData(List<String> companies, String start, String end);

    List<NormalizedStockData> getNormalizedStockData(List<String> companies, Integer month, Integer year);

}
