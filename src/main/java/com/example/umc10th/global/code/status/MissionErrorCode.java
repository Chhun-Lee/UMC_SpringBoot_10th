package com.example.umc10th.global.code.status;

import com.example.umc10th.global.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {

    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_1", "해당 미션을 찾을 수 없습니다."),
    MEMBER_MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_2", "해당 회원 미션을 찾을 수 없습니다."),
    MISSION_ALREADY_COMPLETED(HttpStatus.CONFLICT, "MISSION409_1", "이미 완료된 미션입니다."),
    INVALID_MISSION_STATUS(HttpStatus.BAD_REQUEST, "MISSION400_1", "유효하지 않은 미션 상태입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
