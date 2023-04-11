package com.alukyanau.nysestocks.service.rest;

import com.alukyanau.nysestocks.util.NYSEConstants;
import com.alukyanau.nysestocks.util.RequestParameters;
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
        LocalDate startDate = parameters.startDate();
        LocalDate endDate = parameters.endDate();
        return doRequestCSVData(companyCode, startDate, endDate);
    }

    private String doRequestCSVData(String companyCode, LocalDate startDate, LocalDate endDate) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                NYSEConstants.URL_TEMPLATE,
                String.class,
                companyCode,
                startDate.format(FORMATTER),
                endDate.format(FORMATTER),
                Math.abs(Period.between(startDate, endDate).getDays()));
        return response.getBody();
    }

}
