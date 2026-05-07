package com.example.umc10th.domain.member.exception;

import com.example.umc10th.global.code.status.MemberErrorCode;
import com.example.umc10th.global.exception.CustomException;

public class MemberException extends CustomException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
