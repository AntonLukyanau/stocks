package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.dto.StockNormalizedDTO;

import java.util.List;

public interface NormalizeDateService {

    List<StockNormalizedDTO> getNormalizedDTOS(List<String> companies, String start, String end);

}
