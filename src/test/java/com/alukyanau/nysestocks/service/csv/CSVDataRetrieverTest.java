package com.alukyanau.nysestocks.service.csv;

import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.util.URLUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CSVDataRetrieverTest {

    @InjectMocks
    private CSVDataRetriever csvDataRetriever;

    @Mock
    private URLUtil urlUtil;

    @Test
    void testRetrieveDataWithoutStartAndEndParameters() {
        //given
        RequestParameters parameters = new RequestParameters(
                "epam", null, null);
        //when
        //then
    }

    @Test
    void testRetrieveDataWithCorrectParameters() {
        //given
        RequestParameters parameters = new RequestParameters(
                "epam", LocalDate.now(), LocalDate.now().minusDays(1));
        //when
        //then
    }

}