package com.example.umc10th.domain.mission.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MissionReqDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InProgressMissionRequestDTO {

        @NotNull
        @Positive
        private Long memberId;

        @NotNull
        @Min(0)
        private Integer page;

        @NotNull
        @Min(1)
        @Max(50)
        private Integer size;
    }
}
