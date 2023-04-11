package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.service.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.NormalizeDateService;
import com.alukyanau.nysestocks.service.ParameterService;
import com.alukyanau.nysestocks.service.StatisticService;
import com.alukyanau.nysestocks.util.RequestParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StockDataControllerTest {

    @InjectMocks
    private StockDataController stockDataController;

    private MockMvc mvc;
    @Mock
    private NormalizeDateService normalizeSortedDateService;
    @Mock
    private ParameterService<RequestParameters> stockParameterService;
    @Mock
    private DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    @Mock
    private StatisticService<StockStatistic, StockDataDTO> stockStatisticService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends StockDataController> aClass = stockDataController.getClass();
        Field companies = aClass.getDeclaredField("companies");
        companies.setAccessible(true);
        companies.set(stockDataController, List.of("aapl", "epam", "msft", "ibm"));
        mvc = MockMvcBuilders.standaloneSetup(stockDataController).build();

    }

    @Test
    void testGetStockStatisticByCompany() throws Exception {
        // given
        String url = "/api/v1/stocks/statistics/epam/period?startdate=04/10/2023&enddate=04/12/2023";
        String company = "epam";
        LocalDate newest = LocalDate.of(2023, 4, 12);
        LocalDate oldest = LocalDate.of(2023, 4, 10);
        BigDecimal biggestPrice = BigDecimal.valueOf(350.0);
        BigDecimal lowestPrice = BigDecimal.valueOf(100.0);
        RequestParameters parameters = new RequestParameters(
                company,
                oldest,
                newest);
        StockDataDTO stock1 = StockDataDTO.builder()
                .companyCode(company)
                .date(oldest)
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(lowestPrice)
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO stock2 = StockDataDTO.builder()
                .companyCode(company)
                .date(LocalDate.of(2022, 4, 11))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(lowestPrice)
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO stock3 = StockDataDTO.builder()
                .companyCode(company)
                .date(newest)
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(biggestPrice)
                .minPrice(BigDecimal.valueOf(150.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> dtos = List.of(stock1, stock2, stock3);
        StockStatistic statistic = StockStatistic.builder()
                .company(company)
                .newest(newest)
                .oldest(oldest)
                .max(biggestPrice)
                .min(lowestPrice)
                .build();
        String expectedJson = """
                {
                  "company": "epam",
                  "newest": [2023,04,12],
                  "oldest": [2023,04,10],
                  "max": 350.0,
                  "min": 100.0
                }
                """;
        // when
        when(stockParameterService.fillParameters(any())).thenReturn(parameters);
        when(stockDataRetrievalProcessor.retrievalProcess(parameters)).thenReturn(dtos);
        when(stockStatisticService.aggregate(dtos)).thenReturn(statistic);
        ResultActions actions = mvc.perform(get(url));
        // then
        MvcResult mvcResult = actions
                .andExpectAll(
                        status().isOk(),
                        content().json(expectedJson))
                .andReturn();
    }

    @Test
    void testGetStockStatisticByCompanyWithIncorrectCompany() throws Exception {
        // given
        String url = "/api/v1/stocks/statistics/incorrectCompany/period?startdate=04/10/2023&enddate=04/12/2023";

        // when
        ResultActions actions = mvc.perform(get(url));
        // then
        actions.andExpectAll(
                status().isBadRequest()
        );
        verifyNoInteractions(stockParameterService, stockDataRetrievalProcessor, stockStatisticService);
    }

}