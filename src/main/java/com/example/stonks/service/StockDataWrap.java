package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;

public record StockDataWrap(String source, String company, NYSEResultFrequency resultFrequency) {
}
