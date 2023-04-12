package com.alukyanau.nysestocks.service.resolving;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.infrastructure.NYSEConstants;
import com.alukyanau.nysestocks.model.FixedDateRange;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.ParameterService;
import com.alukyanau.nysestocks.service.StatisticAggregator;
import com.alukyanau.nysestocks.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
