package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.NormalizedStockData;
import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.service.NormalizeDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StockNormalizedValueControllerTest {

    @InjectMocks
    private StockNormalizedValueController controller;

    @Mock
    private NormalizeDateService normalizeSortedDateService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = controller.getClass();
        Field companies = aClass.getDeclaredField("companies");
        companies.setAccessible(true);
        companies.set(controller, List.of("aapl", "epam", "msft", "ibm"));
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetSortedStocksData() throws Exception {
        // given
        String path = "/api/v1/stocks/normalized/range?startdate=04/10/2023&enddate=04/12/2023";
        NormalizedStockData stock1 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.06));
        NormalizedStockData stock2 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.05));
        NormalizedStockData stock3 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.03));
        List<NormalizedStockData> normalizedStocksData = Arrays.asList(stock1, stock2, stock3);
        String expectedJson = """
                [
                  {
                    "stockDataDTO": {
                      "companyCode": null,
                      "date": null,
                      "startPrice": null,
                      "maxPrice": null,
                      "minPrice": null,
                      "endPrice": null,
                      "volume": null
                    },
                    "normalizedValue": 0.06
                  },
                  {
                    "stockDataDTO": {
                      "companyCode": null,
                      "date": null,
                      "startPrice": null,
                      "maxPrice": null,
                      "minPrice": null,
                      "endPrice": null,
                      "volume": null
                    },
                    "normalizedValue": 0.05
                  },
                  {
                    "stockDataDTO": {
                      "companyCode": null,
                      "date": null,
                      "startPrice": null,
                      "maxPrice": null,
                      "minPrice": null,
                      "endPrice": null,
                      "volume": null
                    },
                    "normalizedValue": 0.03
                  }
                ]
                """;
        // when
        when(normalizeSortedDateService.getNormalizedStockData(any(), any(String.class), any(String.class)))
                .thenReturn(normalizedStocksData);
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpectAll(
                status().isOk(),
                content().json(expectedJson)
        );
    }

    @Test
    void testGetSortedStocksDataForCurrentMonth() throws Exception {
        // given
        String path = "/api/v1/stocks/normalized/latest";
        NormalizedStockData stock1 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.06));
        NormalizedStockData stock2 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.05));
        NormalizedStockData stock3 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.03));
        List<NormalizedStockData> normalizedStocksData = Arrays.asList(stock1, stock2, stock3);
        String expectedJson = """
                [
                  {
                    "stockDataDTO": {
                      "companyCode": null,
                      "date": null,
                      "startPrice": null,
                      "maxPrice": null,
                      "minPrice": null,
                      "endPrice": null,
                      "volume": null
                    },
                    "normalizedValue": 0.06
                  },
                  {
                    "stockDataDTO": {
                      "companyCode": null,
                      "date": null,
                      "startPrice": null,
                      "maxPrice": null,
                      "minPrice": null,
                      "endPrice": null,
                      "volume": null
                    },
                    "normalizedValue": 0.05
                  },
                  {
                    "stockDataDTO": {
                      "companyCode": null,
                      "date": null,
                      "startPrice": null,
                      "maxPrice": null,
                      "minPrice": null,
                      "endPrice": null,
                      "volume": null
                    },
                    "normalizedValue": 0.03
                  }
                ]
                """;
        // when
        when(normalizeSortedDateService.getNormalizedStockData(
                any(), any(Integer.class), any(Integer.class))).thenReturn(normalizedStocksData);
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpectAll(
                status().isOk(),
                content().json(expectedJson)
        );
    }

    @Test
    void testGetStockWithHighestValue() throws Exception {
        // given
        String path = "/api/v1/stocks/normalized/highest?date=04/12/2023";
        NormalizedStockData stock1 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.06));
        NormalizedStockData stock2 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.05));
        NormalizedStockData stock3 = new NormalizedStockData(StockDataDTO.builder().build(), BigDecimal.valueOf(0.03));
        List<NormalizedStockData> normalizedStocksData = Arrays.asList(stock1, stock2, stock3);
        String expectedJson = """
                {
                  "stockDataDTO": {
                    "companyCode": null,
                    "date": null,
                    "startPrice": null,
                    "maxPrice": null,
                    "minPrice": null,
                    "endPrice": null,
                    "volume": null
                  },
                  "normalizedValue": 0.06
                }
                """;
        // when
        when(normalizeSortedDateService.getNormalizedStockData(
                any(), any(String.class), any(String.class))).thenReturn(normalizedStocksData);
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpectAll(
                status().isOk(),
                content().json(expectedJson)
        );
    }

    @Test
    void testGetStockWithHighestValueNoContent() throws Exception {
        // given
        String path = "/api/v1/stocks/normalized/highest?date=04/12/2077";
        // when
        when(normalizeSortedDateService.getNormalizedStockData(
                any(), any(String.class), any(String.class))).thenReturn(Collections.emptyList());
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpect(status().isNoContent());
    }
}