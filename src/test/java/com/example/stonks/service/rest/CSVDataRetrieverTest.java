package com.example.stonks.service.rest;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.service.WorkDaysResolver;
import com.example.stonks.util.NYSEConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVDataRetrieverTest {

    @InjectMocks
    private CSVDataRetriever csvDataRetriever;

    @Mock
    private WorkDaysResolver workDaysResolver;
    @Mock
    private RestTemplate restTemplate;

    //given
    //when
    //then
    @Test
    void testRetrieveDataWithMissingFrequencyParameter() {
        //given
        Map<String, Object> parameters = Map.of(NYSEConstants.COMPANY_PARAMETER, "epam");
        //when
        String retrievedData = csvDataRetriever.retrieveData(parameters);
        //then
        assertTrue(retrievedData.isEmpty());
    }

    @Test
    void testRetrieveDataWithoutStartAndEndParameters() {
        //given
        Map<String, Object> parameters = Map.of(
                NYSEConstants.COMPANY_PARAMETER, "epam",
                NYSEConstants.FREQUENCY_PARAMETER, NYSEResultFrequency.DAILY
        );
        //when
        when(workDaysResolver.resolveLastWorkDayBefore(any())).thenReturn(LocalDate.now().minusDays(1));
        when(restTemplate.getForEntity(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(ResponseEntity.ok("some data"));
        String retrievedData = csvDataRetriever.retrieveData(parameters);
        //then
        assertEquals("some data", retrievedData);
    }

}