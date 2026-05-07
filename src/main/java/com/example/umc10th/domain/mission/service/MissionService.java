package com.example.umc10th.domain.mission.service;

import com.example.umc10th.domain.mission.dto.MissionResDTO;

public interface MissionService {

    MissionResDTO.AvailableMissionListDTO getAvailableMissions(Long memberId, Long locationId, Long cursor, Integer size);

    MissionResDTO.MissionCompleteResultDTO completeMission(Long memberMissionId);
}
