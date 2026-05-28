package com.example.umc10th.domain.member.controller;

import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.global.code.status.MemberSuccessCode;
import com.example.umc10th.domain.member.service.MemberService;
import com.example.umc10th.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "신규 회원을 등록합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<CustomResponse<MemberResDTO.SignUpResultDTO>> signUp(
            @Valid @RequestBody MemberReqDTO.SignUpDTO request
    ) {
        MemberResDTO.SignUpResultDTO result = memberService.signUp(request);
        return CustomResponse.ok(MemberSuccessCode.MEMBER_SIGN_UP_CREATED, result);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT access token을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<CustomResponse<MemberResDTO.LoginResultDTO>> login(
            @Valid @RequestBody MemberReqDTO.LoginDTO request
    ) {
        MemberResDTO.LoginResultDTO result = memberService.login(request);
        return CustomResponse.ok(MemberSuccessCode.MEMBER_LOGIN_OK, result);
    }
}
