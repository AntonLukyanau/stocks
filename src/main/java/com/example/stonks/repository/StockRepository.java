package com.example.stonks.repository;

import com.example.stonks.entity.StockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockData, Long> {
}
