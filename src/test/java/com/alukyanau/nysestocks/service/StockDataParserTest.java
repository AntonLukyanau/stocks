package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.util.StockDataWrap;
import com.alukyanau.nysestocks.validator.CSVValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockDataParserTest {

    @InjectMocks
    private StockDataParser parser;

    @Mock
    private CSVValidator validator;

    @Test
    void testParseReturnsEmptyListWhenHeaderIsInvalid() {
        // Given
        String csvData = """
                Invalid Header
                04/01/2023,"100.0","110.0","90.0","105.0","100000"
                """;
        StockDataWrap wrapper = new StockDataWrap(csvData, "ABC");

        // When
        when(validator.validate(any())).thenReturn(false);
        List<StockDataDTO> stockDataList = parser.parse(wrapper);

        // Then
        assertTrue(stockDataList.isEmpty());
    }

    @Test
    void testParseReturnsEmptyListWhenValuesAreInvalid() {
        // Given
        String csvData = """
                Date,Open,High,Low,Close,Volume
                04/01/2023,invalid,110.0,90.0,105.0,100000
                """;
        StockDataWrap wrapper = new StockDataWrap(csvData, "ABC");

        // When
        when(validator.validate(any())).thenReturn(false);
        List<StockDataDTO> stockDataList = parser.parse(wrapper);

        // Then
        assertTrue(stockDataList.isEmpty());
    }

    @Test
    void testParseReturnsCorrectData() {
        // Given
        String csvData = """
                Date,Open,High,Low,Close,Volume
                04/01/2023,"100.0","110.0","90.0","105.0","100000"
                """;
        StockDataWrap wrapper = new StockDataWrap(csvData, "ABC");

        // When
        when(validator.validate(any())).thenReturn(true);
        List<StockDataDTO> stockDataList = parser.parse(wrapper);

        // Then
        assertEquals(1, stockDataList.size());
        StockDataDTO stockData = stockDataList.get(0);
        assertEquals("ABC", stockData.getCompanyCode());
        assertEquals(LocalDate.of(2023, 4, 1), stockData.getDate());
        assertEquals(BigDecimal.valueOf(100.0), stockData.getStartPrice());
        assertEquals(BigDecimal.valueOf(110.0), stockData.getMaxPrice());
        assertEquals(BigDecimal.valueOf(90.0), stockData.getMinPrice());
        assertEquals(BigDecimal.valueOf(105.0), stockData.getEndPrice());
        assertEquals(100000L, stockData.getVolume());
    }

    @Test
    void testParseHandlesVolumeWithCommas() {
        // Given
        String csvData = """
                Date,Open,High,Low,Close,Volume
                04/01/2023,"100.0","110.0","90.0","105.0","1,000,000"
                """;
        StockDataWrap wrapper = new StockDataWrap(csvData, "ABC");

        // When
        when(validator.validate(any())).thenReturn(true);
        List<StockDataDTO> stockDataList = parser.parse(wrapper);

        // Then
        assertEquals(1, stockDataList.size());
        StockDataDTO stockData = stockDataList.get(0);
        assertEquals(1000000L, stockData.getVolume());
    }

    @Test
    void testParseHandlesMultipleRows() {
        // Given
        String csvData = """
                Date,Open,High,Low,Close,Volume
                04/01/2023,"100.0","110.0","90.0","105.0","100,000"
                04/02/2023,"105.0","115.0","95.0","100.0","150,000"
                """;
        StockDataWrap wrapper = new StockDataWrap(csvData, "ABC");
        // When
        when(validator.validate(any())).thenReturn(true);
        List<StockDataDTO> stockDataList = parser.parse(wrapper);

        // Then
        assertEquals(2, stockDataList.size());

        // First row
        StockDataDTO stockData1 = stockDataList.get(0);
        assertEquals("ABC", stockData1.getCompanyCode());
        assertEquals(LocalDate.of(2023, 4, 1), stockData1.getDate());
        assertEquals(BigDecimal.valueOf(100.0), stockData1.getStartPrice());
        assertEquals(BigDecimal.valueOf(110.0), stockData1.getMaxPrice());
        assertEquals(BigDecimal.valueOf(90.0), stockData1.getMinPrice());
        assertEquals(BigDecimal.valueOf(105.0), stockData1.getEndPrice());
        assertEquals(100000L, stockData1.getVolume());

        // Second row
        StockDataDTO stockData2 = stockDataList.get(1);
        assertEquals("ABC", stockData2.getCompanyCode());
        assertEquals(LocalDate.of(2023, 4, 2), stockData2.getDate());
        assertEquals(BigDecimal.valueOf(105.0), stockData2.getStartPrice());
        assertEquals(BigDecimal.valueOf(115.0), stockData2.getMaxPrice());
        assertEquals(BigDecimal.valueOf(95.0), stockData2.getMinPrice());
        assertEquals(BigDecimal.valueOf(100.0), stockData2.getEndPrice());
        assertEquals(150000L, stockData2.getVolume());
    }

}
