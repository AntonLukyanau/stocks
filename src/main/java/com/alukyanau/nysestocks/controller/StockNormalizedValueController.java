package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.NormalizedStockData;
import com.alukyanau.nysestocks.service.statistic.NormalizeDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
                    Find stocks for entered date range.
                    If date range is incorrectly specified default date range is used.
                    Default date range is from last work day of nyse to today.
                    Then calculate normalize value for each stock and sort stocks by normalized value
                    """)
    @ApiResponse(responseCode = "200", description = "Return all found data")
    @Parameter(name = "startdate",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found from this date or from last work day in case empty value",
            example = "02/15/2023")
    @Parameter(name = "enddate",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found to this date or to today in case empty value",
            example = "02/19/2023")
    @GetMapping(path = "/range", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NormalizedStockData>> getSortedStocksData(
            @Nullable @RequestParam("startdate") String start,
            @Nullable @RequestParam("enddate") String end
    ) {
        List<NormalizedStockData> normalizedDTOS =
                normalizeSortedDateService.getNormalizedStockData(companies, start, end);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @Operation(
            summary = """
                    Retrieve sorted by normalized value stock data for the last month by all tracked companies
                    """,
            description = """
                    Find stocks for the current month.
                    Then calculate normalize value for each stock and sort the stocks by normalized value
                    """)
    @ApiResponse(responseCode = "200", description = "Return all found data")
    @GetMapping(path = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiResponse(responseCode = "200", description = "Return all found data")
    @ApiResponse(responseCode = "204", description = "Data was not found. NYSE probably did not work at this date")
    @Parameter(name = "date",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this date. In case the empty value stocks will be found by last work day",
            example = "02/15/2023")
    @ApiResponse(responseCode = "200", description = "Respond if at least one stock was found")
    @GetMapping(path = "/highest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NormalizedStockData> getStockWithHighestValue(
            @Nullable @RequestParam("date") String date
    ) {
        return normalizeSortedDateService.getNormalizedStockData(companies, date, date)
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
