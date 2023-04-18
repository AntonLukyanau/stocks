package com.alukyanau.nysestocks.service.statistic;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.model.FixedDateRange;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.retrieving.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.retrieving.ParameterService;
import com.alukyanau.nysestocks.service.statistic.StatisticAggregator;
import com.alukyanau.nysestocks.service.statistic.StockStatisticService;
import com.alukyanau.nysestocks.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockStatisticServiceTest {

    @InjectMocks
    private StockStatisticService statisticService;

    @Mock
    private ParameterService<RequestParameters> stockParameterService;
    @Mock
    private DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    @Mock
    private StatisticAggregator<StockStatistic, StockDataDTO> stockStatisticAggregator;
    @Mock
    private DateUtil dateUtil;

    @Test
    void testGetStockStatisticByDates() {
        // given
        String company = "epam";
        String startDate = "04/10/2023";
        String endDate = "04/12/2023";
        // when
        statisticService.getStockStatistic(company, startDate, endDate);
        // then
        verify(stockParameterService, times(1)).fillParameters(any(), any(), any());
        verify(stockDataRetrievalProcessor, times(1)).retrievalProcess(any());
        verify(stockStatisticAggregator, times(1)).aggregate(any());
    }

    @Test
    void testGetStockStatisticByMonth() {
        // given
        String company = "epam";
        Integer month = 3;
        Integer year = 2023;
        // when
        when(dateUtil.resolveOneMonthFixedPeriod(any(), any()))
                .thenReturn(new FixedDateRange(
                        LocalDate.of(2023, 3, 1),
                        LocalDate.of(2023, 3, 31)));
        statisticService.getStockStatistic(company, month, year);
        // then
        verify(dateUtil, times(1)).resolveOneMonthFixedPeriod(any(), any());
        verify(stockParameterService, times(1)).fillParameters(any(), any(), any());
        verify(stockDataRetrievalProcessor, times(1)).retrievalProcess(any());
        verify(stockStatisticAggregator, times(1)).aggregate(any());
    }
}