package com.alukyanau.nysestocks.util;

import com.alukyanau.nysestocks.model.RequestParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class URLUtilTest {

    @InjectMocks
    private URLUtil urlUtil;

    @Mock
    private DateUtil dateUtil;

    @Test
    void completeURL() {
        // given
        RequestParameters parameters = new RequestParameters(
                "epam",
                LocalDate.of(2023, 4, 10),
                LocalDate.of(2023, 4, 12));
        String expectedURL = "https://www.marketwatch.com/investing/stock/"
                             + "epam/downloaddatapartial"
                             + "?startdate=04/10/2023%2000:00:00"
                             + "&enddate=04/12/2023%2023:59:59"
                             + "&daterange=d2"
                             + "&frequency=p1d"
                             + "&csvdownload=true"
                             + "&downloadpartial=false"
                             + "&newdates=false";
        // when
        when(dateUtil.calculateDaysPeriod(any(), any())).thenReturn(2);
        String url = urlUtil.completeURL(parameters);
        //then
        assertEquals(expectedURL, url);
    }
}