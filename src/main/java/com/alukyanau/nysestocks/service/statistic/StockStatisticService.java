package com.alukyanau.nysestocks.service.statistic;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.infrastructure.NYSEConstants;
import com.alukyanau.nysestocks.model.FixedDateRange;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.retrieving.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.retrieving.ParameterService;
import com.alukyanau.nysestocks.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Delegate retrieve data to DataRetrievalProcessor
 * then delegate calculation statistics to StatisticAggregator based on parameters
 *
 * @see com.alukyanau.nysestocks.service.retrieving.StockDataRetrievalProcessor
 * @see StockStatisticAggregator
 */
@Service
@RequiredArgsConstructor
public class StockStatisticService implements StatisticService<StockStatistic> {

    private final ParameterService<RequestParameters> stockParameterService;
    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final StatisticAggregator<StockStatistic, StockDataDTO> stockStatisticAggregator;
    private final DateUtil dateUtil;

    public StockStatistic getStockStatistic(String companyCode, Integer month, Integer year) {
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        return getStockStatistic(
                companyCode,
                fixedDateRange.start().format(NYSEConstants.DATE_FORMAT),
                fixedDateRange.end().format(NYSEConstants.DATE_FORMAT));
    }

    public StockStatistic getStockStatistic(String companyCode, String start, String end) {
        RequestParameters parameters = stockParameterService.fillParameters(companyCode, start, end);
        List<StockDataDTO> stocks = stockDataRetrievalProcessor.retrievalProcess(parameters);
        return stockStatisticAggregator.aggregate(stocks);
    }
}
