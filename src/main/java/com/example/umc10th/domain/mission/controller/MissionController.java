package com.example.umc10th.domain.mission.controller;

import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.exception.code.MissionSuccessCode;
import com.example.umc10th.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mission", description = "미션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController {

    @Operation(summary = "도전 가능한 미션 목록 조회",
            description = "지역 기반으로 도전 가능한 미션 목록을 커서 페이지네이션으로 조회합니다.")
    @GetMapping
    public ResponseEntity<CustomResponse<MissionResDTO.AvailableMissionListDTO>> getAvailableMissions(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        // TODO: service 연동
        MissionResDTO.AvailableMissionListDTO result = null;
        return CustomResponse.ok(MissionSuccessCode.MISSION_AVAILABLE_LIST_OK, result);
    }
}
