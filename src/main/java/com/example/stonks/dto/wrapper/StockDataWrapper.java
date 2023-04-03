package com.example.stonks.dto.wrapper;

import com.example.stonks.dto.ResultFrequency;

public record StockDataWrapper(String source, String company, ResultFrequency resultFrequency) {
}
