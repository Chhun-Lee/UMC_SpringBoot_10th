package com.example.umc10th.global.code.status;

import com.example.umc10th.global.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewSuccessCode implements BaseSuccessCode {

    REVIEW_CREATED(HttpStatus.CREATED, "REVIEW201_1", "리뷰 작성 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
