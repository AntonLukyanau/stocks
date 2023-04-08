package com.example.stonks.service.rest;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.util.RequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CSVDataRetriever implements DataRetriever<String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private final RestTemplate restTemplate;

    @Override
    public String retrieveData(RequestParameters parameters) {
        if (parameters.isContainsNull()) {
            return "";
        }
        String companyCode = parameters.companyName();
        NYSEResultFrequency frequency = parameters.frequency();
        LocalDate startDate = parameters.startDate();
        LocalDate endDate = parameters.endDate();
        return doRequestCSVData(companyCode, frequency.getUrlParameterValue(), startDate, endDate);
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
