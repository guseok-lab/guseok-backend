package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "members",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_members_provider_provider_id",
            columnNames = {"provider", "provider_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일
    @Column(nullable = false)
    private String email;

    // 닉네임
    @Column(nullable = false)
    private String nickname;

    // 프로필 이미지
    @Column(name = "profile_image_url", length = 1000)
    private String profileImageUrl;

    // KAKAO
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    // 카카오 고유 ID
    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Builder
    public Member(
        String email,
        String nickname,
        String profileImageUrl,
        Provider provider,
        String providerId
    ) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }
}