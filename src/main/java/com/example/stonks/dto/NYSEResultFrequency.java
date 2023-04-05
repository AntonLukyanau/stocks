package com.example.stonks.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NYSEResultFrequency {
    DAILY("p1d"),
    WEEKLY("p7d"),
    MONTHLY("p1m");

    final String urlParameterValue;

}
