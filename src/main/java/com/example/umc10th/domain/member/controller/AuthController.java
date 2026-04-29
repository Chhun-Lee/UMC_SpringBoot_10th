package com.example.umc10th.domain.member.controller;

import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.domain.member.exception.code.MemberSuccessCode;
import com.example.umc10th.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Operation(summary = "회원가입", description = "신규 회원을 등록합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<CustomResponse<MemberResDTO.SignUpResultDTO>> signUp(
            @RequestBody MemberReqDTO.SignUpDTO request
    ) {
        // TODO: service 연동
        MemberResDTO.SignUpResultDTO result = null;
        return CustomResponse.ok(MemberSuccessCode.MEMBER_SIGN_UP_CREATED, result);
    }
}
