package com.alukyanau.nysestocks.util;

import com.alukyanau.nysestocks.dto.NYSEResultFrequency;

public record StockDataWrap(String source, String company, NYSEResultFrequency resultFrequency) {
}
