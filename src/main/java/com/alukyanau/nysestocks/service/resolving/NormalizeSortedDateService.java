package com.alukyanau.nysestocks.service.resolving;

import com.alukyanau.nysestocks.dto.NormalizedStockData;
import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.infrastructure.NYSEConstants;
import com.alukyanau.nysestocks.model.FixedDateRange;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.NormalizeDateService;
import com.alukyanau.nysestocks.service.ParameterService;
import com.alukyanau.nysestocks.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NormalizeSortedDateService implements NormalizeDateService {

    private final ParameterService<RequestParameters> stockParameterService;
    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final DateUtil dateUtil;

    public List<NormalizedStockData> getNormalizedStockData(List<String> companies, String start, String end) {
        List<StockDataDTO> stockDataDTOS = new ArrayList<>();
        for (String companyCode : companies) {
            RequestParameters parameters = stockParameterService.fillParameters(companyCode, start, end);
            stockDataDTOS.addAll(stockDataRetrievalProcessor.retrievalProcess(parameters));
        }
        return createNormalizedSortedData(stockDataDTOS);
    }

    public List<NormalizedStockData> getNormalizedStockData(List<String> companies, Integer month, Integer year) {
        FixedDateRange fixedDateRange = dateUtil.resolveOneMonthFixedPeriod(month, year);
        return getNormalizedStockData(
                companies,
                fixedDateRange.start().format(NYSEConstants.DATE_FORMAT),
                fixedDateRange.end().format(NYSEConstants.DATE_FORMAT));
    }

    private List<NormalizedStockData> createNormalizedSortedData(List<StockDataDTO> stockDataDTOS) {
        return stockDataDTOS.stream()
                .map(this::wrapWithCalculatedNormalizedValue)
                .sorted(Comparator.comparing(NormalizedStockData::getNormalizedValue).reversed())
                .toList();
    }

    private NormalizedStockData wrapWithCalculatedNormalizedValue(StockDataDTO source) {
        if (source.getMinPrice().equals(BigDecimal.ZERO)) {
            return new NormalizedStockData(source, BigDecimal.ZERO);
        }
        BigDecimal subtracted = source.getMaxPrice().subtract(source.getMinPrice());
        BigDecimal divided = subtracted.divide(source.getMinPrice(), MathContext.DECIMAL64);
        return new NormalizedStockData(source, divided);
    }

}
