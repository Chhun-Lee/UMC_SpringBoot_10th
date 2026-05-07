package com.example.umc10th.global.code.status;

import com.example.umc10th.global.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MissionSuccessCode implements BaseSuccessCode {

    MISSION_AVAILABLE_LIST_OK(HttpStatus.OK, "MISSION200_1", "도전 가능한 미션 목록 조회 성공"),
    MISSION_COMPLETE_OK(HttpStatus.OK, "MISSION200_2", "미션 완료 처리 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
