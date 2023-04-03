package com.example.stonks.entity;

import com.example.stonks.dto.ResultFrequency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_data_seq")
    @SequenceGenerator(name = "stock_data_seq", sequenceName = "stock_data_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_code")
    private String companyCode; //for example aapl

    @Column(name = "company_name")
    private String companyName; //for example Apple Inc.

    @Column(name = "at_date")
    private LocalDate date;

    @Column(name = "start_price")
    private BigDecimal startPrice;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "end_price")
    private BigDecimal endPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_frequency")
    private ResultFrequency resultFrequency;

    @Column(name = "volume")
    private Long volume;

}
