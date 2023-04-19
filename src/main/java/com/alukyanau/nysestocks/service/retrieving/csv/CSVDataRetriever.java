package com.alukyanau.nysestocks.service.retrieving.csv;

import com.alukyanau.nysestocks.model.CSVStockData;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.retrieving.DataRetriever;
import com.alukyanau.nysestocks.util.URLUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Retrieve data by URL which is building by parameters {@link RequestParameters}
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class CSVDataRetriever implements DataRetriever<List<CSVStockData>> {

    private final URLUtil urlUtil;

    @Override
    public List<CSVStockData> retrieveData(RequestParameters parameters) {
        if (parameters.isContainsNull()) {
            return Collections.emptyList();
        }
        String url = urlUtil.completeURL(parameters);
        try (InputStreamReader reader = new InputStreamReader(new URL(url).openStream(), StandardCharsets.UTF_8)) {
            CsvToBeanBuilder<CSVStockData> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            return csvToBeanBuilder
                    .withType(CSVStockData.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            log.error("Bad interaction with new-york stock exchange service ", e);
            return Collections.emptyList();
        }
    }

}
