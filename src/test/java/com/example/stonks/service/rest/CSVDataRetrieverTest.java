package com.example.stonks.service.rest;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.service.WorkDaysResolver;
import com.example.stonks.util.RequestParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CSVDataRetrieverTest {

    @InjectMocks
    private CSVDataRetriever csvDataRetriever;

    @Mock
    private WorkDaysResolver workDaysResolver;
    @Mock
    private RestTemplate restTemplate;

    @Test
    void testRetrieveDataWithMissingFrequencyParameter() {
        //given
        RequestParameters parameters = new RequestParameters(
                "epam", null, null, null);
        //when
        String retrievedData = csvDataRetriever.retrieveData(parameters);
        //then
        assertTrue(retrievedData.isEmpty());
    }

    @Test
    void testRetrieveDataWithoutStartAndEndParameters() {
        //given
        RequestParameters parameters = new RequestParameters(
                "epam", NYSEResultFrequency.DAILY, null, null);
        //when
        when(workDaysResolver.resolveLastWorkDayBefore(any())).thenReturn(LocalDate.now().minusDays(1));
        when(restTemplate.getForEntity(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(ResponseEntity.ok("some data"));
        String retrievedData = csvDataRetriever.retrieveData(parameters);
        //then
        assertEquals("some data", retrievedData);
    }

}