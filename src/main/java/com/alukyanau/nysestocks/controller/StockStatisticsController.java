package com.alukyanau.nysestocks.controller;

import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.service.resolving.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    @Parameter(name = "startdate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found from this date",
            example = "02/15/2023")
    @Parameter(name = "enddate",
            in = ParameterIn.QUERY,
            description = "Stocks will be found to this date",
            example = "02/19/2023")
    @GetMapping("/{companyCode}/period")
    public ResponseEntity<StockStatistic> getByCompany(
            @PathVariable String companyCode,
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
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
    @Parameter(name = "month",
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this month",
            example = "4")
    @Parameter(name = "year",
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this year",
            example = "2023")
    @Parameter(name = "companyCode",
            in = ParameterIn.PATH,
            description = "Stocks will be found by this single company. Can be equal to 'all'",
            example = "epam",
            required = true)
    @GetMapping("/{companyCode}/by")
    public ResponseEntity<StockStatistic> getForCompanyByMonthAndYear(
            @PathVariable("companyCode") String companyCode,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year
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
    @Parameter(name = "month",
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this month",
            example = "4")
    @Parameter(name = "year",
            in = ParameterIn.QUERY,
            description = "Stocks will be found by this year",
            example = "2023")
    @GetMapping("/all/by")
    public ResponseEntity<List<StockStatistic>> getAllByMonthAndYear(
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year
    ) {
        return ResponseEntity.ok(companies.stream()
                .map(company -> stockStatisticService.getStockStatistic(company, month, year))
                .toList());
    }

}
