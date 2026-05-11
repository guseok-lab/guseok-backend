package com.guseok.guseokbackend.repository;

import com.guseok.guseokbackend.entity.Member;
import com.guseok.guseokbackend.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
