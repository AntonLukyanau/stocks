package com.example.stonks.service.rest;

import com.example.stonks.service.WorkDaysResolver;
import com.example.stonks.util.NYSEConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CSVDataRetriever implements DataRetriever<String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private final WorkDaysResolver workDaysResolver;
    private final RestTemplate restTemplate;

    @Override
    public String retrieveData(Map<String, Object> parameters) {
        Object companyCode = parameters.get(NYSEConstants.COMPANY_PARAMETER);
        Object frequency = parameters.get(NYSEConstants.FREQUENCY_PARAMETER);
        if (companyCode == null || frequency == null) {
            return "";
        }
        Object startParam = parameters.get(NYSEConstants.START_DATE);
        Object endParam = parameters.get(NYSEConstants.END_DATE);
        LocalDate startDate;
        LocalDate endDate;
        if (startParam == null || endParam == null) {
            startDate = LocalDate.now();
            endDate = workDaysResolver.resolveLastWorkDayBefore(startDate);
        } else {
            startDate = LocalDate.parse(String.valueOf(startParam), FORMATTER);
            endDate = LocalDate.parse(String.valueOf(endParam), FORMATTER);
        }
        return doRequestCSVData(String.valueOf(companyCode), String.valueOf(frequency), startDate, endDate);
    }

    private String doRequestCSVData(String companyCode, String frequency, LocalDate startDate, LocalDate endDate) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                NYSEConstants.URL_TEMPLATE,
                String.class,
                companyCode,
                startDate.format(FORMATTER),
                endDate.format(FORMATTER),
                (Period.between(endDate, startDate).getDays()),
                frequency);
        return response.getBody();
    }

}
