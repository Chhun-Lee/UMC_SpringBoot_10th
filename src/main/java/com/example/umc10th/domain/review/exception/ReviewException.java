package com.example.umc10th.domain.review.exception;

import com.example.umc10th.global.code.status.ReviewErrorCode;
import com.example.umc10th.global.exception.CustomException;

public class ReviewException extends CustomException {

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode);
    }
}
