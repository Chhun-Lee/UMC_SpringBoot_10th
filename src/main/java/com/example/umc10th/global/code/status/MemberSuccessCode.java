package com.example.umc10th.global.code.status;

import com.example.umc10th.global.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements BaseSuccessCode {

    MEMBER_HOME_OK(HttpStatus.OK, "MEMBER200_1", "홈 화면 조회 성공"),
    MEMBER_MISSION_LIST_OK(HttpStatus.OK, "MEMBER200_2", "회원 미션 목록 조회 성공"),
    MEMBER_LOGIN_OK(HttpStatus.OK, "MEMBER200_3", "로그인 성공"),
    MEMBER_SIGN_UP_CREATED(HttpStatus.CREATED, "MEMBER201_1", "회원가입 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
