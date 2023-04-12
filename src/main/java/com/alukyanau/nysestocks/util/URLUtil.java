package com.alukyanau.nysestocks.util;

import com.alukyanau.nysestocks.infrastructure.NYSEConstants;
import com.alukyanau.nysestocks.model.RequestParameters;
import lombok.RequiredArgsConstructor;

@UtilComponent
@RequiredArgsConstructor
public class URLUtil {

    private final DateUtil dateUtil;

    public String completeURL(RequestParameters parameters) {
        return String.format(
                        NYSEConstants.URL_TEMPLATE,
                        parameters.companyName(),
                        parameters.startDate().format(NYSEConstants.DATE_FORMAT),
                        parameters.endDate().format(NYSEConstants.DATE_FORMAT),
                        dateUtil.calculateDaysPeriod(parameters.startDate(), parameters.endDate()))
                .replace(" ", "%20");
    }

}
