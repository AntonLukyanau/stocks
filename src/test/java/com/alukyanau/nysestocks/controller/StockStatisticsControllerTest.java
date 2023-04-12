package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.service.resolving.StatisticService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StockStatisticsControllerTest {

    @InjectMocks
    private StockStatisticsController controller;

    @Mock
    private StatisticService<StockStatistic> stockStatisticService;

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
    void testGetByUnsupportedCompany() throws Exception {
        // given
        String path = "/api/v1/stocks/statistics/unsupported/period?startdate=04/05/2023&enddate=04/10/2023";
        // when
        ResultActions result = mockMvc.perform(get(path));
        // then
        verifyNoInteractions(stockStatisticService);
        result.andExpect(status().isBadRequest());
    }

    @Test
    void testGetByCompany() throws Exception {
        // given
        String path = "/api/v1/stocks/statistics/epam/period?startdate=04/05/2023&enddate=04/10/2023";
        StockStatistic stockStatistic = StockStatistic.builder()
                .company("epam")
                .newest(LocalDate.of(2023, 4, 10))
                .oldest(LocalDate.of(2023, 4, 5))
                .max(BigDecimal.valueOf(300.0))
                .min(BigDecimal.valueOf(280.0))
                .build();
        String expectedJson = """
                {
                  "company": "epam",
                  "newest": [2023,04,10],
                  "oldest": [2023,04,05],
                  "max": 300.0,
                  "min": 280.0
                }
                """;
        // when
        when(stockStatisticService.getStockStatistic(any(), any(String.class), any(String.class)))
                .thenReturn(stockStatistic);
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpectAll(
                status().isOk(),
                content().json(expectedJson)
        );
    }

    @Test
    void getForUnsupportedCompanyByMonthAndYear() throws Exception {
        // given
        String path = "/api/v1/stocks/statistics/unsupported/by?month=3&year=2023";
        // when
        ResultActions result = mockMvc.perform(get(path));
        // then
        verifyNoInteractions(stockStatisticService);
        result.andExpect(status().isBadRequest());
    }

    @Test
    void testGetForCompanyByMonthAndYear() throws Exception {
        // given
        String path = "/api/v1/stocks/statistics/epam/by?month=3&year=2023";
        StockStatistic stockStatistic = StockStatistic.builder()
                .company("epam")
                .newest(LocalDate.of(2023, 3, 31))
                .oldest(LocalDate.of(2023, 3, 1))
                .max(BigDecimal.valueOf(300.0))
                .min(BigDecimal.valueOf(280.0))
                .build();
        String expectedJson = """
                {
                  "company": "epam",
                  "newest": [2023,03,31],
                  "oldest": [2023,03,01],
                  "max": 300.0,
                  "min": 280.0
                }
                """;
        // when
        when(stockStatisticService.getStockStatistic(any(), any(Integer.class), any(Integer.class)))
                .thenReturn(stockStatistic);
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpectAll(
                status().isOk(),
                content().json(expectedJson)
        );
    }

    @Test
    void getAllByMonthAndYear() throws Exception {
        // given
        String path = "/api/v1/stocks/statistics/all/by?month=3&year=2023";
        StockStatistic stockStatistic = StockStatistic.builder()
                .company("aapl")
                .newest(LocalDate.of(2023, 3, 31))
                .oldest(LocalDate.of(2023, 3, 1))
                .max(BigDecimal.valueOf(300.0))
                .min(BigDecimal.valueOf(280.0))
                .build();
        String expectedJson = """
                [
                  {
                    "company": "aapl",
                    "newest": [2023,03,31],
                    "oldest": [2023,03,01],
                    "max": 300.0,
                    "min": 280.0
                  },
                  {
                    "company": "aapl",
                    "newest": [2023,03,31],
                    "oldest": [2023,03,01],
                    "max": 300.0,
                    "min": 280.0
                  },
                  {
                    "company": "aapl",
                    "newest": [2023,03,31],
                    "oldest": [2023,03,01],
                    "max": 300.0,
                    "min": 280.0
                  },
                  {
                    "company": "aapl",
                    "newest": [2023,03,31],
                    "oldest": [2023,03,01],
                    "max": 300.0,
                    "min": 280.0
                  }
                ]
               
                """;
        // when
        when(stockStatisticService.getStockStatistic(any(), any(Integer.class), any(Integer.class)))
                .thenReturn(stockStatistic);
        ResultActions result = mockMvc.perform(get(path));
        // then
        result.andExpectAll(
                status().isOk(),
                content().json(expectedJson)
        );
    }
}