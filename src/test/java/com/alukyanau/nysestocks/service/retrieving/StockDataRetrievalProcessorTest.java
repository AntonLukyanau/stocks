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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        String validData = """
                Date,Open,High,Low,Close,Volume
                01/01/2022,"150.0","200.0","100.0","180.0","50,000"
                """;
        RequestParameters parameters = new RequestParameters(
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));

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
        // then
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
        // then
    }

    @Test
    public void testRetrievalProcessEmptyData() {
        // given
        String emptyData = "";
        RequestParameters parameters = new RequestParameters(
                "aapl",
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022, 2, 9));

        // when
        // then
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
        verifyNoInteractions(requestCache, stockDataRetriever, stockDataConverter, requestRepository);
    }
}
