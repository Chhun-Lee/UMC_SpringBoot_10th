package com.example.umc10th.domain.mission.controller;

import com.example.umc10th.domain.mission.dto.MissionReqDTO;
import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.global.code.status.MissionSuccessCode;
import com.example.umc10th.domain.mission.service.MissionService;
import com.example.umc10th.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MemberMission", description = "회원 미션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member-missions")
public class MemberMissionController {

    private final MissionService missionService;

    @Operation(summary = "미션 성공 처리", description = "회원 미션을 완료 상태로 변경합니다.")
    @PatchMapping("/{memberMissionId}/complete")
    public ResponseEntity<CustomResponse<MissionResDTO.MissionCompleteResultDTO>> completeMission(
            @PathVariable Long memberMissionId
    ) {
        MissionResDTO.MissionCompleteResultDTO result = missionService.completeMission(memberMissionId);
        return CustomResponse.ok(MissionSuccessCode.MISSION_COMPLETE_OK, result);
    }

    @Operation(summary = "진행중인 미션 목록 조회",
            description = "특정 회원의 진행중인 미션 목록을 오프셋 페이지네이션으로 조회합니다.")
    @PostMapping("/in-progress")
    public ResponseEntity<CustomResponse<MissionResDTO.InProgressMissionListDTO>> getInProgressMissions(
            @RequestBody @Valid MissionReqDTO.InProgressMissionRequestDTO request
    ) {
        MissionResDTO.InProgressMissionListDTO result = missionService.getInProgressMissions(request);
        return CustomResponse.ok(MissionSuccessCode.MISSION_IN_PROGRESS_LIST_OK, result);
    }
}
