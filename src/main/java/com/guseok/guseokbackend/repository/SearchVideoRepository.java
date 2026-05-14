package com.guseok.guseokbackend.repository;

import com.guseok.guseokbackend.entity.SearchVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchVideoRepository extends JpaRepository<SearchVideo, Long> {
}