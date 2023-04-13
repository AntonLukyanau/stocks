package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.service.resolving.StatisticService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks/statistics")
@RequiredArgsConstructor
public class StockStatisticsController {

    @Value("${stock.companies}")
    private List<String> companies;

    private final StatisticService<StockStatistic> stockStatisticService;

    @Operation(
            summary = "Retrieve aggregated stock statistic by company for entered period",
            description = """
                    Find stocks by company for entered period.
                    Then find highest price, lowest price, start date and end date through found stocks.
                    """)
    @ApiResponse(responseCode = "200", description = "Return all found data")
    @ApiResponse(responseCode = "400", description = "Company not tracked")
    @Parameter(name = "companyCode",
            in = ParameterIn.PATH,
            description = "Stocks will be found by this single company",
            example = "epam",
            required = true)
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
    @GetMapping(path = "/{companyCode}/period", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockStatistic> getByCompany(
            @PathVariable String companyCode,
            @Nullable @RequestParam("startdate") String start,
            @Nullable @RequestParam("enddate") String end
    ) {
        if (companies.contains(companyCode)) {
            StockStatistic stockStatistic = stockStatisticService.getStockStatistic(companyCode, start, end);
            return ResponseEntity.ok(stockStatistic);
        }
        return ResponseEntity.badRequest().build();
    }


    @Operation(
            summary = """
                    Retrieve statistics by single specified company for whole entered month and year
                    """,
            description = """
                    Find stocks by entered year and month for specified company.
                    Then return aggregated statistics
                    """)
    @ApiResponse(responseCode = "200", description = "Return all found data")
    @ApiResponse(responseCode = "400", description = "Company not tracked")
    @Parameter(name = "companyCode",
            in = ParameterIn.PATH,
            description = "Stocks will be found by this single company",
            example = "epam",
            required = true)
    @Parameter(name = "month",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this month or the current month in case an empty value",
            example = "4")
    @Parameter(name = "year",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this year or the current year in case an empty value",
            example = "2023")
    @GetMapping(path = "/{companyCode}/by", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockStatistic> getForCompanyByMonthAndYear(
            @PathVariable("companyCode") String companyCode,
            @Nullable @RequestParam("month") Integer month,
            @Nullable @RequestParam("year") Integer year
    ) {
        if (companies.contains(companyCode)) {
            return ResponseEntity.ok(stockStatisticService.getStockStatistic(companyCode, month, year));
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = """
                    Retrieve statistics by all tracked companies
                    for whole entered month and year (or for the current month in case not specified month/year)
                    """,
            description = """
                    Find stocks for entered year and month for all tracked company.
                    If month not specified return statistics for current month.
                    If year not specified return statistics for current year.
                    Aggregate statistics for each company
                    """)
    @ApiResponse(responseCode = "200", description = "Return all found data")
    @Parameter(name = "month",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this month or the current month in case an empty value",
            example = "4")
    @Parameter(name = "year",
            allowEmptyValue = true,
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this year or the current year in case an empty value",
            example = "2023")
    @GetMapping(path = "/all/by", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockStatistic>> getAllByMonthAndYear(
            @Nullable @RequestParam("month") Integer month,
            @Nullable @RequestParam("year") Integer year
    ) {
        return ResponseEntity.ok(companies.stream()
                .map(company -> stockStatisticService.getStockStatistic(company, month, year))
                .toList());
    }

}
