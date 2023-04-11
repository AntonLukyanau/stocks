package com.alukyanau.nysestocks.repository;

import com.alukyanau.nysestocks.entity.RequestToNYSE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestToNYSERepository extends JpaRepository<RequestToNYSE, Long> {

    @Query(value = """
            SELECT r from RequestToNYSE r
            ORDER BY r.date DESC
            LIMIT ?1
            """)
    List<RequestToNYSE> findLastRequests(int maxRowNumber);

}
