package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.NormalizedStockData;
import com.alukyanau.nysestocks.service.NormalizeDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks/normalized")
@RequiredArgsConstructor
public class StockNormalizedValueController {

    @Value("${stock.companies}")
    private List<String> companies;

    private final NormalizeDateService normalizeSortedDateService;

    @Operation(
            summary = """
                    Retrieve sorted by normalized value stock data by all tracked companies
                    """,
            description = """
                    Find stocks for entered period.
                    If period is incorrectly specified default period is used.
                    Default period is from last work day of nyse to today.
                    Then calculate normalize value for each stock and sort stocks by normalized value
                    """)
    @Parameter(name = "startdate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found from this date",
            example = "02/15/2023")
    @Parameter(name = "enddate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found to this date",
            example = "02/19/2023")
    @GetMapping("/range")
    public ResponseEntity<List<NormalizedStockData>> getSortedStocksData(
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        List<NormalizedStockData> normalizedDTOS =
                normalizeSortedDateService.getNormalizedStockData(companies, start, end);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @Operation(
            summary = """
                    Retrieve sorted by normalized value stock data for last month by all tracked companies
                    """,
            description = """
                    Find stocks for the current month.
                    Then calculate normalize value for each stock and sort stocks by normalized value
                    """)
    @GetMapping("/latest")
    public ResponseEntity<List<NormalizedStockData>> getSortedStocksDataForCurrentMonth() {
        return ResponseEntity.ok(normalizeSortedDateService.getNormalizedStockData(
                companies,
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear()));
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
            example = "02/15/2023")
    @ApiResponse(responseCode = "200", description = "Respond if at least one stock was found")
    @GetMapping("/highest")
    public ResponseEntity<NormalizedStockData> getStockWithHighestValue(
            @RequestParam("date") String date
    ) {
        return normalizeSortedDateService.getNormalizedStockData(companies, date, date)
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
