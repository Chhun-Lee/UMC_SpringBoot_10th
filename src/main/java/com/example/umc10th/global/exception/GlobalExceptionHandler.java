package com.example.umc10th.global.exception;

import com.example.umc10th.global.apiPayload.CustomResponse;
import com.example.umc10th.global.code.BaseErrorCode;
import com.example.umc10th.global.code.status.GlobalErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<Void>> handleCustomException(CustomException e) {
        log.warn("CustomException: {}", e.getMessage());
        BaseErrorCode code = e.getErrorCode();
        return CustomResponse.fail(code, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe ->
                errors.put(fe.getField(), fe.getDefaultMessage()));
        return CustomResponse.fail(GlobalErrorStatus._VALIDATION_ERROR, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("Type mismatch: {}", e.getMessage());
        return CustomResponse.fail(GlobalErrorStatus._BAD_REQUEST, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomResponse<Void>> handleNotFound(NoHandlerFoundException e) {
        return CustomResponse.fail(GlobalErrorStatus._NOT_FOUND, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());
        return CustomResponse.fail(GlobalErrorStatus._BAD_REQUEST, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return CustomResponse.fail(GlobalErrorStatus._INTERNAL_SERVER_ERROR, null);
    }
}
