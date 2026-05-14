package com.guseok.guseokbackend.repository;

import com.guseok.guseokbackend.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}
