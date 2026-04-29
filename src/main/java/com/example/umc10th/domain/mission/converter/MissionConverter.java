package com.example.umc10th.domain.mission.converter;

import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.entity.Mission;
import com.example.umc10th.domain.mission.entity.mapping.MemberMission;

import java.util.List;

public class MissionConverter {

    public static MissionResDTO.AvailableMissionDTO toAvailableMissionDTO(Mission mission) {
        return MissionResDTO.AvailableMissionDTO.builder()
                .missionId(mission.getId())
                .shopName(mission.getStore().getShopName())
                .condition(mission.getCondition())
                .point(mission.getPoint())
                .deadline(mission.getDeadline())
                .build();
    }

    public static MissionResDTO.AvailableMissionListDTO toAvailableMissionListDTO(
            List<Mission> missions, Long nextCursor, boolean hasNext) {
        List<MissionResDTO.AvailableMissionDTO> missionDTOs = missions.stream()
                .map(MissionConverter::toAvailableMissionDTO)
                .toList();
        return MissionResDTO.AvailableMissionListDTO.builder()
                .missions(missionDTOs)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    public static MissionResDTO.MissionCompleteResultDTO toMissionCompleteResultDTO(MemberMission memberMission) {
        return MissionResDTO.MissionCompleteResultDTO.builder()
                .memberMissionId(memberMission.getId())
                .isCompleted(memberMission.getIsCompleted())
                .build();
    }
}
