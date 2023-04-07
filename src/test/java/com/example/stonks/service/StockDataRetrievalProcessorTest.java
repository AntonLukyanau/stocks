package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.entity.StockData;
import com.example.stonks.repository.StockRepository;
import com.example.stonks.service.rest.DataRetriever;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.util.StockDataWrap;
import com.example.stonks.validator.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockDataRetrievalProcessorTest {

    @Mock
    private Parser<StockDataDTO, StockDataWrap> stockDataParser;

    @Mock
    private DataRetriever<String> stockDataRetriever;

    @Mock
    private Validator<String> csvValidator;

    @Mock
    private Converter<StockDataDTO, StockData> stockDataConverter;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockDataRetrievalProcessor processor;

    @Test
    public void testRetrievalProcessSuccess() {
        // given
        String validData = """
                Date,Open,High,Low,Close,Volume
                01/01/2022,"150.0","200.0","100.0","180.0","50,000"
                """;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NYSEConstants.COMPANY_PARAMETER, "aapl");
        parameters.put(NYSEConstants.FREQUENCY_PARAMETER, NYSEResultFrequency.DAILY);

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
        when(csvValidator.validate(validData)).thenReturn(true);
        when(stockDataParser.parse(dataWrapper)).thenReturn(expectedStockDataDTOS);
        when(stockDataConverter.convert(any())).thenReturn(stockData);

        // when
        List<StockDataDTO> actualStockDataDTOS = processor.retrievalProcess(parameters);

        // then
        verify(stockDataRetriever).retrieveData(parameters);
        verify(csvValidator).validate(validData);
        verify(stockDataParser).parse(dataWrapper);
        verify(stockDataConverter).convert(expectedStockDataDTOS.get(0));
        verify(stockRepository).saveAll(Collections.singletonList(stockData));
        assertEquals(expectedStockDataDTOS, actualStockDataDTOS);
    }

    @Test
    public void testRetrievalProcessFailure() {
        // given
        String invalidData = "invalid,data,here";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NYSEConstants.COMPANY_PARAMETER, "aapl");
        parameters.put(NYSEConstants.FREQUENCY_PARAMETER, NYSEResultFrequency.DAILY);

        when(stockDataRetriever.retrieveData(parameters)).thenReturn(invalidData);
        when(csvValidator.validate(invalidData)).thenReturn(false);

        // when
        List<StockDataDTO> actualStockDataDTOS = processor.retrievalProcess(parameters);

        // then
        verify(stockDataRetriever).retrieveData(parameters);
        verify(csvValidator).validate(invalidData);
        verifyNoInteractions(stockDataParser);
        verifyNoInteractions(stockDataConverter);
        verifyNoInteractions(stockRepository);
        assertTrue(actualStockDataDTOS.isEmpty());
    }

    @Test
    public void testRetrievalProcessEmptyData() {
        // given
        String emptyData = "";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NYSEConstants.COMPANY_PARAMETER, "aapl");
        parameters.put(NYSEConstants.FREQUENCY_PARAMETER, NYSEResultFrequency.DAILY);

        StockDataWrap dataWrapper = new StockDataWrap(emptyData, "aapl", NYSEResultFrequency.DAILY);
        when(stockDataRetriever.retrieveData(parameters)).thenReturn(emptyData);
        when(csvValidator.validate(emptyData)).thenReturn(true);
        when(stockDataParser.parse(dataWrapper)).thenReturn(Collections.emptyList());

        // when
        List<StockDataDTO> actualStockDataDTOS = processor.retrievalProcess(parameters);

        // then
        verify(stockDataRetriever).retrieveData(parameters);
        verify(csvValidator).validate(emptyData);
        verify(stockDataParser).parse(dataWrapper);
        verifyNoInteractions(stockDataConverter);
        verifyNoInteractions(stockRepository);
        assertNotNull(actualStockDataDTOS);
        assertTrue(actualStockDataDTOS.isEmpty());
    }

    @Test
    public void testRetrievalProcessMissingParameter() {
        // given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(NYSEConstants.COMPANY_PARAMETER, "aapl");

        // when
        List<StockDataDTO> stockDataDTOS = processor.retrievalProcess(parameters);
        // then
        assertTrue(stockDataDTOS.isEmpty());
        verifyNoInteractions(stockDataRetriever, csvValidator, stockDataParser, stockDataConverter, stockRepository);
    }
}
