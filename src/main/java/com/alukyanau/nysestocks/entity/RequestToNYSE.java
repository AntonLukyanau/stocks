package com.alukyanau.nysestocks.entity;

import com.alukyanau.nysestocks.dto.NYSEResultFrequency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "request_to_nyse")
public class RequestToNYSE {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_to_nyse_id_seq")
    @SequenceGenerator(name = "request_to_nyse_id_seq", sequenceName = "request_to_nyse_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "at_date")
    private LocalDate date;

    @Column(name = "company_param")
    private String companyParameter;

    @Column(name = "frequency_param")
    private NYSEResultFrequency frequencyParameter;

    @Column(name = "start_date_param")
    private LocalDate startDateParameter;

    @Column(name = "end_date_param")
    private LocalDate endDateParameter;

    @OneToMany(mappedBy = "request")
    private List<StockData> stocks;

}
