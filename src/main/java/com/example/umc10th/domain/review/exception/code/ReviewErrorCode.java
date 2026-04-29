package com.example.umc10th.domain.review.exception.code;

import com.example.umc10th.global.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW404_1", "해당 리뷰를 찾을 수 없습니다."),
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW404_2", "해당 가게를 찾을 수 없습니다."),
    INVALID_STAR_RATING(HttpStatus.BAD_REQUEST, "REVIEW400_1", "별점은 0.5 ~ 5.0 사이의 0.5 단위 값이어야 합니다."),
    PHOTO_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REVIEW500_1", "리뷰 사진 업로드에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
