package com.alukyanau.nysestocks.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CSVStockData {

    @CsvDate(value = "MM/dd/yyyy")
    @CsvBindByName(column = "Date")
    private LocalDate date;

    @CsvBindByName(column = "Open")
    private BigDecimal open;

    @CsvBindByName(column = "High")
    private BigDecimal high;

    @CsvBindByName(column = "Low")
    private BigDecimal low;

    @CsvBindByName(column = "Close")
    private BigDecimal close;

    @CsvNumber("###,##")
    @CsvBindByName(column = "Volume")
    private Long volume;
}
