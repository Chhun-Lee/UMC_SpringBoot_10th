package com.example.umc10th.global.code.status;

import com.example.umc10th.global.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalSuccessStatus implements BaseSuccessCode {

    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "리소스가 정상적으로 생성되었습니다."),
    _NO_CONTENT(HttpStatus.NO_CONTENT, "COMMON204", "응답 본문이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
