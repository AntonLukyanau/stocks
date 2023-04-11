package com.alukyanau.nysestocks.repository;

import com.alukyanau.nysestocks.entity.RequestToNYSE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestToNYSERepository extends JpaRepository<RequestToNYSE, Long> {
}
