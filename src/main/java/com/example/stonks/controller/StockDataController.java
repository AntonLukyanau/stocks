package com.example.stonks.controller;

import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockNormalizedDTO;
import com.example.stonks.dto.StockStatistic;
import com.example.stonks.service.DataRetrievalProcessor;
import com.example.stonks.service.NormalizeDateService;
import com.example.stonks.service.StatisticService;
import com.example.stonks.service.ParameterService;
import com.example.stonks.util.RequestParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockDataController {

    @Value("${stock.companies}")
    private List<String> companies;

    private final NormalizeDateService normalizeSortedDateService;
    private final ParameterService<RequestParameters> stockParameterService;
    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final StatisticService<StockStatistic, StockDataDTO> stockStatisticService;

    @Operation(
            summary = "Retrieve aggregated stock statistic by company for entered period",
            description = """
                    Find stocks by company for entered period.
                    Then find highest price, lowest price, start date and end date through found stocks.
                    """)
    @Parameter(name = "startdate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found from this date",
            required = true,
            example = "02/15/2023")
    @Parameter(name = "enddate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found to this date",
            required = true,
            example = "02/19/2023")
    @GetMapping("/statistics/{companyCode}/period")
    public ResponseEntity<StockStatistic> getStockStatisticByCompany(
            @PathVariable String companyCode,
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        RequestParameters parameters = stockParameterService.fillParameters(companyCode, start, end);
        List<StockDataDTO> stocks = stockDataRetrievalProcessor.retrievalProcess(parameters);
        StockStatistic stockStatistic = stockStatisticService.aggregate(stocks);
        return ResponseEntity.ok(stockStatistic);
    }

    @Operation(
            summary = "Retrieve sorted by normalized value stock data by all tracked companies for entered period",
            description = """
                    Find stocks for entered period.
                    Then calculate normalize value for each stock and sort that
                    """)
    @Parameter(name = "startdate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found from this date",
            required = true,
            example = "02/15/2023")
    @Parameter(name = "enddate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found to this date",
            required = true,
            example = "02/19/2023")
    @GetMapping("/all/period")
    public ResponseEntity<List<StockNormalizedDTO>> getAllCompanyStocksData(
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        List<StockNormalizedDTO> normalizedDTOS = normalizeSortedDateService
                .getNormalizedDTOS(companies, start, end);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @Operation(
            summary = "Retrieve highest by normalized value stock data by all tracked companies for entered date",
            description = """
                    Find stocks for entered date.
                    Then calculate normalize value for each stock and find highest
                    """)
    @Parameter(name = "date",
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this date",
            required = true,
            example = "02/15/2023")
    @ApiResponse(responseCode = "200", description = "Respond if at least one stock was found")
    @ApiResponse(responseCode = "204", description = """
            Respond if nothing was found
            or retrieved invalid data from external service
            """)
    @GetMapping("/all/highest")
    public ResponseEntity<StockNormalizedDTO> getHighestStockBySpecificDay(@RequestParam("date") String date) {
        return normalizeSortedDateService.getNormalizedDTOS(companies, date, date)
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
