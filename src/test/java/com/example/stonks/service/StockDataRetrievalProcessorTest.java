package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.entity.StockData;
import com.example.stonks.repository.RequestToNYSERepository;
import com.example.stonks.service.rest.DataRetriever;
import com.example.stonks.util.RequestParameters;
import com.example.stonks.util.StockDataWrap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockDataRetrievalProcessorTest {

    @Mock
    private Parser<StockDataDTO, StockDataWrap> stockDataParser;

    @Mock
    private DataRetriever<String> stockDataRetriever;


    @Mock
    private Converter<StockDataDTO, StockData> stockDataConverter;

    @Mock
    private RequestToNYSERepository requestRepository;

    @InjectMocks
    private StockDataRetrievalProcessor retrievalProcessor;

    @Test
    public void testRetrievalProcessSuccess() {
        // given
        String validData = """
                Date,Open,High,Low,Close,Volume
                01/01/2022,"150.0","200.0","100.0","180.0","50,000"
                """;
        RequestParameters parameters = new RequestParameters(
                "aapl", NYSEResultFrequency.DAILY, null, null);

        StockDataWrap dataWrapper = new StockDataWrap(validData, "aapl", NYSEResultFrequency.DAILY);
        List<StockDataDTO> expectedStockDataDTOS = List.of(
                StockDataDTO.builder()
                        .companyCode("aapl")
                        .date(LocalDate.of(2022, 1, 1))
                        .startPrice(BigDecimal.valueOf(150.0))
                        .maxPrice(BigDecimal.valueOf(200.0))
                        .minPrice(BigDecimal.valueOf(100.0))
                        .endPrice(BigDecimal.valueOf(180.0))
                        .volume(50000L)
                        .build()
        );
        StockData stockData = StockData.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 1))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        when(stockDataRetriever.retrieveData(parameters)).thenReturn(validData);
        when(stockDataParser.parse(dataWrapper)).thenReturn(expectedStockDataDTOS);
        when(stockDataConverter.convert(any())).thenReturn(stockData);

        // when
        List<StockDataDTO> actualStockDataDTOS = retrievalProcessor.retrievalProcess(parameters);

        // then
        verify(stockDataRetriever).retrieveData(parameters);
        verify(stockDataParser).parse(dataWrapper);
        verify(stockDataConverter).convert(expectedStockDataDTOS.get(0));
        verify(requestRepository).save(any());
        assertEquals(expectedStockDataDTOS, actualStockDataDTOS);
    }

    @Test
    public void testRetrievalProcessFailure() {
        // given
        String invalidData = "invalid,data,here";
        RequestParameters parameters = new RequestParameters(
                "aapl", NYSEResultFrequency.DAILY, null, null);

        // when
        when(stockDataRetriever.retrieveData(parameters)).thenReturn(invalidData);
        List<StockDataDTO> actualStockDataDTOS = retrievalProcessor.retrievalProcess(parameters);

        // then
        verify(stockDataRetriever).retrieveData(parameters);
        verifyNoInteractions(stockDataConverter);
        verifyNoInteractions(requestRepository);
        assertTrue(actualStockDataDTOS.isEmpty());
    }

    @Test
    public void testRetrievalProcessEmptyData() {
        // given
        String emptyData = "";
        RequestParameters parameters = new RequestParameters(
                "aapl", NYSEResultFrequency.DAILY, null, null);

        StockDataWrap dataWrapper = new StockDataWrap(emptyData, "aapl", NYSEResultFrequency.DAILY);
        when(stockDataRetriever.retrieveData(parameters)).thenReturn(emptyData);
        when(stockDataParser.parse(dataWrapper)).thenReturn(Collections.emptyList());

        // when
        List<StockDataDTO> actualStockDataDTOS = retrievalProcessor.retrievalProcess(parameters);

        // then
        verify(stockDataRetriever).retrieveData(parameters);
        verify(stockDataParser).parse(dataWrapper);
        verifyNoInteractions(stockDataConverter);
        verifyNoInteractions(requestRepository);
        assertNotNull(actualStockDataDTOS);
        assertTrue(actualStockDataDTOS.isEmpty());
    }

    @Test
    public void testRetrievalProcessMissingParameter() {
        // given
        RequestParameters parameters = new RequestParameters(
                "aapl", null, null, null);
        // when
        List<StockDataDTO> stockDataDTOS = retrievalProcessor.retrievalProcess(parameters);
        // then
        assertTrue(stockDataDTOS.isEmpty());
        verifyNoInteractions(stockDataRetriever, stockDataParser, stockDataConverter, requestRepository);
    }
}
