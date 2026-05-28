package com.example.umc10th.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class MemberResDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class HomeDTO {
        private String memberName;
        private String region;
        private Integer point;
        private MissionProgressDTO missionProgress;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MissionProgressDTO {
        private Integer completed;
        private Integer total;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberMissionListDTO {
        private List<MemberMissionDTO> missions;
        private Long nextCursor;
        private Boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberMissionDTO {
        private Long memberMissionId;
        private Long missionId;
        private String shopName;
        private Integer point;
        private String condition;
        private LocalDate deadline;
        private Boolean isCompleted;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SignUpResultDTO {
        private Long memberId;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LoginResultDTO {
        private Long memberId;
        private String accessToken;
        private String tokenType;
    }
}
