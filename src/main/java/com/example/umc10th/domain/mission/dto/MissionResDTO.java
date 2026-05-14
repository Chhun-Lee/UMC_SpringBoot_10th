package com.example.umc10th.domain.mission.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class MissionResDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class AvailableMissionListDTO {
        private List<AvailableMissionDTO> missions;
        private Long nextCursor;
        private Boolean hasNext;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class AvailableMissionDTO {
        private Long missionId;
        private String shopName;
        private String condition;
        private Integer point;
        private LocalDate deadline;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MissionCompleteResultDTO {
        private Long memberMissionId;
        private Boolean isCompleted;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InProgressMissionListDTO {
        private List<AvailableMissionDTO> missions;
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
        private Boolean hasNext;
    }
}
