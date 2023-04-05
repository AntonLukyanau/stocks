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

    private final WorkDaysResolver workDaysResolver;

    @Override
    public String retrieveData(Map<String, Object> parameters) {
        String companyCode = String.valueOf(parameters.get(NYSEConstants.COMPANY_PARAMETER));
        String frequency = String.valueOf(parameters.get(NYSEConstants.FREQUENCY_PARAMETER));
        LocalDate startDate = (LocalDate) parameters.get(NYSEConstants.START_DATE);
        LocalDate endDate = (LocalDate) parameters.get(NYSEConstants.END_DATE);
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate == null) {
            startDate = workDaysResolver.resolveLastWorkDayBefore(endDate);
        }
        return requestCSVData(companyCode, frequency, startDate, endDate);
    }

    private String requestCSVData(String companyCode, String frequency, LocalDate startDate, LocalDate endDate) {
        RestTemplate restTemplate = new RestTemplate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        ResponseEntity<String> response = restTemplate.getForEntity(
                NYSEConstants.URL_TEMPLATE,
                String.class,
                companyCode,
                startDate.format(formatter),
                endDate.format(formatter),
                (Period.between(endDate, startDate).getDays()),
                frequency);
        return response.getBody();
    }

}
