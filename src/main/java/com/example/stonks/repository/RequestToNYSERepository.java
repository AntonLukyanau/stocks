package com.example.stonks.repository;

import com.example.stonks.entity.RequestToNYSE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestToNYSERepository extends JpaRepository<RequestToNYSE, Long> {
}
