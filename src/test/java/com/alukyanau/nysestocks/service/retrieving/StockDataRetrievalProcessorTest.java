package com.alukyanau.nysestocks.service.retrieving;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.infrastructure.cache.RequestCache;
import com.alukyanau.nysestocks.model.CSVStockData;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.repository.RequestToNYSERepository;
import com.alukyanau.nysestocks.repository.StockRepository;
import com.alukyanau.nysestocks.service.csv.DataRetriever;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockDataRetrievalProcessorTest {

    @InjectMocks
    private StockDataRetrievalProcessor retrievalProcessor;

    @Mock
    private DataRetriever<List<CSVStockData>> stockDataRetriever;
    @Mock
    private Converter<StockData, StockDataDTO> stockDataConverter;
    @Mock
    private RequestToNYSERepository requestRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private RequestCache<RequestParameters, List<StockData>> requestCache;

    @Test
    public void testRetrievalProcessSuccess() {
        // given
        RequestParameters parameters = new RequestParameters(
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));

        StockDataDTO stockDataDTO = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 2, 9))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> expectedStockDataDTOS = List.of(stockDataDTO);
        CSVStockData expectedCSV = new CSVStockData(
                stockDataDTO.getDate(),
                stockDataDTO.getStartPrice(),
                stockDataDTO.getMaxPrice(),
                stockDataDTO.getMinPrice(),
                stockDataDTO.getEndPrice(),
                stockDataDTO.getVolume());
        // when
        when(requestCache.containsKey(any())).thenReturn(false);
        when(stockDataRetriever.retrieveData(any())).thenReturn(List.of(expectedCSV));
        when(stockDataConverter.convert(any())).thenReturn(stockDataDTO);
        List<StockDataDTO> result = retrievalProcessor.retrievalProcess(parameters);
        // then
        verify(requestCache, times(1)).containsKey(any());
        verify(stockDataRetriever, times(1)).retrieveData(any());
        verify(requestRepository, times(1)).save(any());
        verify(stockRepository, times(1)).saveAll(any());
        verify(requestCache, times(1)).store(any(), any());
        verify(stockDataConverter, times(1)).convert(any());
        assertEquals(expectedStockDataDTOS, result);
    }

    @Test
    public void testRetrievalProcessSuccessFromCache() {
        // given
        RequestParameters parameters = new RequestParameters(
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));
        StockData stockData = StockData.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 2, 9))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO stockDataDTO = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 2, 9))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> expectedStockDataDTOS = List.of(stockDataDTO);
        // when
        when(requestCache.containsKey(any())).thenReturn(true);
        when(requestCache.getBy(any())).thenReturn(List.of(stockData));
        when(stockDataConverter.convert(any())).thenReturn(stockDataDTO);
        List<StockDataDTO> result = retrievalProcessor.retrievalProcess(parameters);
        // then
        verifyNoInteractions(stockDataRetriever, requestRepository, stockRepository);
        assertEquals(expectedStockDataDTOS, result);
    }

    @Test
    public void testRetrievalProcessFailed() {
        // given
        RequestParameters parameters = new RequestParameters(
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));
        // when
        when(requestCache.containsKey(any())).thenReturn(false);
        when(stockDataRetriever.retrieveData(any())).thenReturn(Collections.emptyList());
        List<StockDataDTO> result = retrievalProcessor.retrievalProcess(parameters);
        // then
        verify(requestCache, times(1)).containsKey(any());
        verify(stockDataRetriever, times(1)).retrieveData(any());
        verifyNoInteractions(requestRepository, stockRepository, stockDataConverter);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRetrievalProcessMissingParameter() {
        // given
        RequestParameters parameters = new RequestParameters(
                "aapl", null, null);
        // when
        List<StockDataDTO> stockDataDTOS = retrievalProcessor.retrievalProcess(parameters);
        // then
        assertTrue(stockDataDTOS.isEmpty());
        verifyNoInteractions(requestCache, stockDataRetriever, stockDataConverter, requestRepository, stockRepository);
    }
}
