package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "missing_persons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissingPerson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer weight;

    @Column(length = 1000)
    private String appearanceDescription;

    @Column(length = 1000)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissingStatus status;

    @Builder
    public MissingPerson(
        Member member,
        String name,
        Integer age,
        Gender gender,
        Integer height,
        Integer weight,
        String appearanceDescription,
        String photoUrl
    ) {
        this.member = member;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.appearanceDescription = appearanceDescription;
        this.photoUrl = photoUrl;
        this.status = MissingStatus.SEARCHING;
    }
}
