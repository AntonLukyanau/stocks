package com.example.stonks.service;

import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockNormalizedDTO;
import com.example.stonks.util.RequestParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NormalizeSortedDateServiceTest {

    @InjectMocks
    private NormalizeSortedDateService normalizeSortedDateService;

    @Mock
    private DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    @Mock
    private Converter<StockDataDTO, StockNormalizedDTO> stockNormalizeDTOConverter;

    @Test
    public void testGetNormalizedDTOS() {
        // given
        StockDataDTO aapl1 = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 1))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO aapl2 = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 1))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> stockDataDTOs = List.of(aapl1, aapl2);
        StockNormalizedDTO e1 = new StockNormalizedDTO(aapl1, BigDecimal.valueOf((200.0 - 100.0) / 100.0));
        StockNormalizedDTO e2 = new StockNormalizedDTO(aapl2, BigDecimal.valueOf((250.0 - 100.0) / 100.0));
        List<StockNormalizedDTO> stockNormalizedDTOs = List.of(e2, e1);
        // when
        when(stockDataRetrievalProcessor.retrievalProcess(any(RequestParameters.class))).thenReturn(stockDataDTOs);
        when(stockNormalizeDTOConverter.convert(any(StockDataDTO.class)))
                .thenReturn(stockNormalizedDTOs.get(0), stockNormalizedDTOs.get(1));

        List<StockNormalizedDTO> result = normalizeSortedDateService.getNormalizedDTOS(
                List.of("AAPL"), "01/01/2022", "01/02/2022");

        // then
        assertEquals(stockNormalizedDTOs, result);
        assertEquals(e2, result.get(0));
        assertEquals(e1, result.get(1));

    }
}