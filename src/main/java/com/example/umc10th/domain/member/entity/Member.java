package com.example.umc10th.domain.member.entity;

import com.example.umc10th.domain.member.enums.Gender;
import com.example.umc10th.domain.member.enums.SocialType;
import com.example.umc10th.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tb_member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private java.time.LocalDate birth;

    private String address;

    @Enumerated(EnumType.STRING)
    private SocialType provider;

    private String providerId;

    private String addressDetail;

    @Column(nullable = false)
    private Integer point = 0;

    @Column(length = 13)
    private String phoneNumber;

    private String profile;

    @Column(columnDefinition = "json")
    private String setting;
}
