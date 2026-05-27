package com.guseok.guseokbackend.repository;

import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    List<SearchResult> findBySearch(Search search);
}