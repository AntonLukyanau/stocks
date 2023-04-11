package com.alukyanau.nysestocks.entity;

import com.alukyanau.nysestocks.dto.NYSEResultFrequency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_data")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_data_id_seq")
    @SequenceGenerator(name = "stock_data_id_seq", sequenceName = "stock_data_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestToNYSE request;

    @Column(name = "company_code")
    private String companyCode; //for example aapl

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
    private NYSEResultFrequency resultFrequency;

    @Column(name = "volume")
    private Long volume;

}
