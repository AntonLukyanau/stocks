package com.alukyanau.nysestocks.service.retrieving;

import com.alukyanau.nysestocks.infrastructure.NYSEConstants;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.resolving.WorkDaysResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Fill request parameters
 *
 * @see com.alukyanau.nysestocks.model.RequestParameters
 */
@Service
@RequiredArgsConstructor
public class StockParameterService implements ParameterService<RequestParameters> {

    private final WorkDaysResolver nyseWorkDaysResolver;

    @Override
    public RequestParameters fillParameters(String... parameters) {
        String company = parameters[0];
        String start = parameters[1];
        String end = parameters[2];
        LocalDate endDate = end == null || end.isBlank()
                ? LocalDate.now()
                : LocalDate.parse(end, NYSEConstants.DATE_FORMAT);
        LocalDate startDate = start == null || start.isBlank()
                ? nyseWorkDaysResolver.resolveLastWorkDayBefore(endDate)
                : LocalDate.parse(start, NYSEConstants.DATE_FORMAT);
        return new RequestParameters(company, startDate, endDate);
    }
}
