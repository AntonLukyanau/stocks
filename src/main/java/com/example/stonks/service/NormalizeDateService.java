package com.example.stonks.service;

import com.example.stonks.dto.StockNormalizedDTO;

import java.util.List;

public interface NormalizeDateService {

    List<StockNormalizedDTO> getNormalizedDTOS(List<String> companies, String start, String end);

}
