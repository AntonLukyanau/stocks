package com.alukyanau.nysestocks.service.retrieving.csv;

import com.alukyanau.nysestocks.model.CSVStockData;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.util.URLUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVDataRetrieverTest {

    @InjectMocks
    private CSVDataRetriever csvDataRetriever;

    @Mock
    private URLUtil urlUtil;

    @Test
    void testRetrieveDataWithoutStartAndEndParameters() {
        // given
        RequestParameters parameters = new RequestParameters(
                "epam", null, null);
        // when
        List<CSVStockData> retrievedData = csvDataRetriever.retrieveData(parameters);
        // then
        verifyNoInteractions(urlUtil);
        assertTrue(retrievedData.isEmpty());
    }

    @Test
    void testRetrieveDataWithCorrectParameters() throws IOException {
        // given
        RequestParameters parameters = new RequestParameters(
                "epam", LocalDate.now(), LocalDate.now().minusDays(1));
        Path tempFile = Files.createTempFile("test", ".csv");
        try (FileWriter fileWriter = new FileWriter(tempFile.toFile())) {
            fileWriter.append("""
                    Date,Open,High,Low,Close,Volume
                    03/29/2023,"31.0","33.0","30.0","32.0","100,000"
                    03/28/2023,"31.0","33.0","30.0","32.0","100,000"
                    """);
        }
        String path = tempFile.toUri().toURL().toString();
        CSVStockData csvStockData1 = new CSVStockData(
                LocalDate.of(2023, 3, 29),
                BigDecimal.valueOf(31.0),
                BigDecimal.valueOf(33.0),
                BigDecimal.valueOf(30.0),
                BigDecimal.valueOf(32.0),
                100_000L);
        CSVStockData csvStockData2 = new CSVStockData(
                LocalDate.of(2023, 3, 28),
                BigDecimal.valueOf(31.0),
                BigDecimal.valueOf(33.0),
                BigDecimal.valueOf(30.0),
                BigDecimal.valueOf(32.0),
                100_000L);
        List<CSVStockData> expectedObjects = List.of(csvStockData1, csvStockData2);
        // when
        when(urlUtil.completeURL(any())).thenReturn(path);
        List<CSVStockData> retrievedData = csvDataRetriever.retrieveData(parameters);
        // then
        assertEquals(expectedObjects, retrievedData);
        // clear used
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testRetrieveDataThrowIOException() {
        // given
        RequestParameters parameters = new RequestParameters(
                "epam", LocalDate.now(), LocalDate.now().minusDays(1));
        // when
        when(urlUtil.completeURL(any())).thenReturn("shouldBeThrowIOException");
        List<CSVStockData> retrievedData = csvDataRetriever.retrieveData(parameters);
        // then
        assertTrue(retrievedData.isEmpty());
    }

}