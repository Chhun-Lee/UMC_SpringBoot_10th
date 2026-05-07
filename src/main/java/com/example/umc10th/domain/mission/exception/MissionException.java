package com.example.umc10th.domain.mission.exception;

import com.example.umc10th.global.code.status.MissionErrorCode;
import com.example.umc10th.global.exception.CustomException;

public class MissionException extends CustomException {

    public MissionException(MissionErrorCode errorCode) {
        super(errorCode);
    }
}
