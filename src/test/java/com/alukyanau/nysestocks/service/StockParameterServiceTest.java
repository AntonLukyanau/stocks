package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.util.RequestParameters;
import com.alukyanau.nysestocks.dto.NYSEResultFrequency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockParameterServiceTest {

    @InjectMocks
    private StockParameterService stockParameterService;

    @Mock
    private WorkDaysResolver workDaysResolver;

    @Test
    void testFillParametersWithFullData() {
        // given
        String company = "epam";
        String start = "03/01/2023";
        String end = "04/01/2023";
        // when
        RequestParameters parameters = stockParameterService.fillParameters(company, start, end);
        // then
        assertEquals(company, parameters.companyName());
        assertEquals(NYSEResultFrequency.DAILY, parameters.frequency());
        assertEquals(LocalDate.of(2023, 3, 1), parameters.startDate());
        assertEquals(LocalDate.of(2023, 4, 1), parameters.endDate());
    }

    @Test
    void testFillParametersWithBlankDates() {
        // given
        String company = "epam";
        String start = "";
        String end = "\n";
        LocalDate today = LocalDate.now();
        LocalDate lastWorkingDay = LocalDate.now().minusDays(1);
        // when
        when(workDaysResolver.resolveLastWorkDayBefore(any()))
                .thenReturn(lastWorkingDay);
        RequestParameters parameters = stockParameterService.fillParameters(company, start, end);
        // then
        assertEquals(company, parameters.companyName());
        assertEquals(NYSEResultFrequency.DAILY, parameters.frequency());
        assertEquals(today, parameters.endDate());
        assertEquals(lastWorkingDay, parameters.startDate());
    }

}