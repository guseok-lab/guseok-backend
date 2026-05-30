package com.guseok.guseokbackend.repository;

import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchVideo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchVideoRepository extends JpaRepository<SearchVideo, Long> {
    List<SearchVideo> findBySearch(Search search);
}