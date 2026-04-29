package com.example.umc10th.domain.member.dto;

import com.example.umc10th.domain.member.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class MemberReqDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SignUpDTO {
        private String name;
        private Gender gender;
        private LocalDate birth;
        private String address;
        private String addressDetail;
        private String email;
        private String phoneNumber;
        private String nickname;
        private List<Long> preferFoodIds;
        private List<Long> termIds;
    }
}
