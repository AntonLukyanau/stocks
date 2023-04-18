package com.alukyanau.nysestocks.service.statistic;

import com.alukyanau.nysestocks.dto.NormalizedStockData;
import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.model.FixedDateRange;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.retrieving.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.retrieving.ParameterService;
import com.alukyanau.nysestocks.service.statistic.NormalizeSortedDateService;
import com.alukyanau.nysestocks.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NormalizeSortedDateServiceTest {

    @InjectMocks
    private NormalizeSortedDateService normalizeSortedDateService;

    @Mock
    @SuppressWarnings("unused") // It should be mocked. No matter what parameters will be returned, even null
    private ParameterService<RequestParameters> stockParameterService;
    @Mock
    private DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    @Mock
    private DateUtil dateUtil;

    @Test
    public void testGetNormalizedStockDataByDates() {
        // given
        StockDataDTO aapl = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 2))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO epam = StockDataDTO.builder()
                .companyCode("epam")
                .date(LocalDate.of(2022, 1, 2))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> stockDataDTOs = List.of(aapl, epam);
        NormalizedStockData e1 = new NormalizedStockData(aapl, BigDecimal.valueOf(1));
        NormalizedStockData e2 = new NormalizedStockData(epam, BigDecimal.valueOf(1.5));
        List<NormalizedStockData> expectedData = List.of(e2, e1);
        List<String> companies = List.of("aapl", "epam");
        // when
        when(stockDataRetrievalProcessor.retrievalProcess(any()))
                .thenReturn(List.of(stockDataDTOs.get(0)))
                .thenReturn(List.of(stockDataDTOs.get(1)));
        List<NormalizedStockData> result = normalizeSortedDateService.getNormalizedStockData(
                companies, "01/02/2022", "01/02/2022");
        // then
        assertEquals(expectedData, result);
        assertEquals(e2, result.get(0));
        assertEquals(e1, result.get(1));
    }

    @Test
    public void testGetNormalizedStockDataByMonth() {
        // given
        StockDataDTO aapl = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 2))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO epam = StockDataDTO.builder()
                .companyCode("epam")
                .date(LocalDate.of(2022, 1, 2))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> stockDataDTOs = List.of(aapl, epam);
        NormalizedStockData e1 = new NormalizedStockData(aapl, BigDecimal.valueOf(1));
        NormalizedStockData e2 = new NormalizedStockData(epam, BigDecimal.valueOf(1.5));
        List<NormalizedStockData> expectedData = List.of(e2, e1);
        List<String> companies = List.of("aapl", "epam");
        // when
        when(dateUtil.resolveOneMonthFixedPeriod(any(), any()))
                .thenReturn(new FixedDateRange(
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 31)
                ));
        when(stockDataRetrievalProcessor.retrievalProcess(any()))
                .thenReturn(List.of(stockDataDTOs.get(0)))
                .thenReturn(List.of(stockDataDTOs.get(1)));
        List<NormalizedStockData> result = normalizeSortedDateService.getNormalizedStockData(
                companies, 1, 2022);
        // then
        assertEquals(expectedData, result);
        assertEquals(e2, result.get(0));
        assertEquals(e1, result.get(1));
    }

    @Test
    public void testGetNormalizedStockDataWithZeroMinPrice() {
        // given
        StockDataDTO epam = StockDataDTO.builder()
                .companyCode("epam")
                .date(LocalDate.of(2022, 1, 2))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(BigDecimal.valueOf(0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> stockDataDTOs = List.of(epam);
        List<String> companies = List.of("epam");
        // when
        when(dateUtil.resolveOneMonthFixedPeriod(any(), any()))
                .thenReturn(new FixedDateRange(
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 31)
                ));
        when(stockDataRetrievalProcessor.retrievalProcess(any()))
                .thenReturn(List.of(stockDataDTOs.get(0)));
        List<NormalizedStockData> result = normalizeSortedDateService.getNormalizedStockData(
                companies, 1, 2022);
        // then
        assertEquals(BigDecimal.ZERO, result.get(0).getNormalizedValue());
    }
}