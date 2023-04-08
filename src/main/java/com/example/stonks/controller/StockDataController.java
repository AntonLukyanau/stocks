package com.example.stonks.controller;

import com.example.stonks.dto.StockNormalizedDTO;
import com.example.stonks.service.NormalizeDateService;
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

    @GetMapping("/{companyCode}/period")
    public ResponseEntity<List<StockNormalizedDTO>> getStocksDataByCompany(
            @PathVariable String companyCode,
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        List<StockNormalizedDTO> normalizedDTOS = normalizeSortedDateService
                .getNormalizedDTOS(List.of(companyCode), start, end);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @GetMapping("/all/period")
    public ResponseEntity<List<StockNormalizedDTO>> getAllCompanyStocksData(
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        List<StockNormalizedDTO> normalizedDTOS = normalizeSortedDateService
                .getNormalizedDTOS(companies, start, end);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @GetMapping("/all/highest")
    public ResponseEntity<StockNormalizedDTO> getHighestStockBySpecificDay(@RequestParam("date") String date) {
        return normalizeSortedDateService.getNormalizedDTOS(companies, date, date)
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
