package com.guseok.guseokbackend.repository;

import com.guseok.guseokbackend.entity.Member;
import com.guseok.guseokbackend.entity.MissingPerson;
import com.guseok.guseokbackend.entity.MissingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissingPersonRepository extends JpaRepository<MissingPerson, Long> {
    List<MissingPerson> findByMember(Member member);
    List<MissingPerson> findByStatus(MissingStatus status);
    long countByStatus(MissingStatus status);
}
