package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.repository.RequestToNYSERepository;
import com.alukyanau.nysestocks.service.cache.RequestCache;
import com.alukyanau.nysestocks.service.rest.DataRetriever;
import com.alukyanau.nysestocks.util.RequestParameters;
import com.alukyanau.nysestocks.util.StockDataWrap;
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

    @Mock
    private RequestCache<RequestParameters, List<StockDataDTO>> requestCache;

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
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));

        StockDataWrap dataWrapper = new StockDataWrap(validData, "aapl");
        List<StockDataDTO> expectedStockDataDTOS = List.of(
                StockDataDTO.builder()
                        .companyCode("aapl")
                        .date(LocalDate.of(2022, 2, 9))
                        .startPrice(BigDecimal.valueOf(150.0))
                        .maxPrice(BigDecimal.valueOf(200.0))
                        .minPrice(BigDecimal.valueOf(100.0))
                        .endPrice(BigDecimal.valueOf(180.0))
                        .volume(50000L)
                        .build()
        );
        StockData stockData = StockData.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 2, 9))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        // when
        when(requestCache.containsKey(any())).thenReturn(false);
        when(stockDataRetriever.retrieveData(parameters)).thenReturn(validData);
        when(stockDataParser.parse(dataWrapper)).thenReturn(expectedStockDataDTOS);
        when(stockDataConverter.convert(any())).thenReturn(stockData);

        List<StockDataDTO> actualStockDataDTOS = retrievalProcessor.retrievalProcess(parameters);

        // then
        verify(requestCache).containsKey(parameters);
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
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));

        // when
        when(requestCache.containsKey(any())).thenReturn(false);
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
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));

        StockDataWrap dataWrapper = new StockDataWrap(emptyData, "aapl");
        // when
        when(requestCache.containsKey(any())).thenReturn(false);
        when(stockDataRetriever.retrieveData(parameters)).thenReturn(emptyData);
        when(stockDataParser.parse(dataWrapper)).thenReturn(Collections.emptyList());

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
                "aapl", null, null);
        // when
        when(requestCache.containsKey(any())).thenReturn(false);
        List<StockDataDTO> stockDataDTOS = retrievalProcessor.retrievalProcess(parameters);
        // then
        assertTrue(stockDataDTOS.isEmpty());
        verifyNoInteractions(stockDataRetriever, stockDataParser, stockDataConverter, requestRepository);
    }
}
