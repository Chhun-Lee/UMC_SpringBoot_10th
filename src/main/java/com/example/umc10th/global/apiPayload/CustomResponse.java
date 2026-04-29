package com.example.umc10th.global.apiPayload;

import com.example.umc10th.global.code.BaseCode;
import com.example.umc10th.global.code.BaseErrorCode;
import com.example.umc10th.global.code.BaseSuccessCode;
import com.example.umc10th.global.code.status.GlobalSuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class CustomResponse<T> {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T result;

    public static <T> ResponseEntity<CustomResponse<T>> ok(T result) {
        return ok(GlobalSuccessStatus._OK, result);
    }

    public static <T> ResponseEntity<CustomResponse<T>> created(T result) {
        return ok(GlobalSuccessStatus._CREATED, result);
    }

    public static <T> ResponseEntity<CustomResponse<T>> ok(BaseSuccessCode code, T result) {
        CustomResponse<T> body = new CustomResponse<>(true, code.getCode(), code.getMessage(), result);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    public static <T> ResponseEntity<CustomResponse<T>> fail(BaseErrorCode code, T result) {
        CustomResponse<T> body = new CustomResponse<>(false, code.getCode(), code.getMessage(), result);
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    public static CustomResponse<Void> failBody(BaseErrorCode code) {
        return new CustomResponse<>(false, code.getCode(), code.getMessage(), null);
    }

    public static CustomResponse<Void> failBody(String code, String message) {
        return new CustomResponse<>(false, code, message, null);
    }

    public static <T> CustomResponse<T> successBody(BaseCode code, T result) {
        return new CustomResponse<>(true, code.getCode(), code.getMessage(), result);
    }
}
