package com.example.umc10th.domain.member.controller;

import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.domain.member.exception.code.MemberSuccessCode;
import com.example.umc10th.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    @Operation(summary = "홈 화면 조회", description = "회원 이름, 지역, 포인트, 미션 진행도를 조회합니다.")
    @GetMapping("/home")
    public ResponseEntity<CustomResponse<MemberResDTO.HomeDTO>> getHome(
            @RequestParam(required = false) Long locationId
    ) {
        // TODO: service 연동
        MemberResDTO.HomeDTO result = null;
        return CustomResponse.ok(MemberSuccessCode.MEMBER_HOME_OK, result);
    }

    @Operation(summary = "회원 미션 목록 조회", description = "ACTIVE/COMPLETED 상태별 회원 미션 목록을 커서 기반으로 조회합니다.")
    @GetMapping("/missions")
    public ResponseEntity<CustomResponse<MemberResDTO.MemberMissionListDTO>> getMemberMissions(
            @RequestParam String status,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        // TODO: service 연동
        MemberResDTO.MemberMissionListDTO result = null;
        return CustomResponse.ok(MemberSuccessCode.MEMBER_MISSION_LIST_OK, result);
    }
}
