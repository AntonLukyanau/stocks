package com.alukyanau.nysestocks.service.rest;

import com.alukyanau.nysestocks.util.RequestParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CSVDataRetrieverTest {

    @InjectMocks
    private CSVDataRetriever csvDataRetriever;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void testRetrieveDataWithoutStartAndEndParameters() {
        //given
        RequestParameters parameters = new RequestParameters(
                "epam", null, null);
        //when
        String retrievedData = csvDataRetriever.retrieveData(parameters);
        //then
        assertNotNull(retrievedData);
        assertTrue(retrievedData.isEmpty());
    }

    @Test
    void testRetrieveDataWithCorrectParameters() {
        //given
        RequestParameters parameters = new RequestParameters(
                "epam", LocalDate.now(), LocalDate.now().minusDays(1));
        //when
        when(restTemplate.getForEntity(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(ResponseEntity.ok("some data"));
        String retrievedData = csvDataRetriever.retrieveData(parameters);
        //then
        assertEquals("some data", retrievedData);
    }

}