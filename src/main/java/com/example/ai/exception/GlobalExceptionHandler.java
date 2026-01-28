package com.example.ai.exception;

import com.example.ai.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理AI服务异常
     */
    @ExceptionHandler(AiServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleAiServiceException(AiServiceException e) {
        log.error("AI service error: ", e);
        return ApiResponse.error(500, "AI服务异常: " + e.getMessage());
    }

    /**
     * 处理WebClient异常
     */
    @ExceptionHandler(WebClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse<Void> handleWebClientException(WebClientException e) {
        log.error("WebClient error: ", e);
        return ApiResponse.error(503, "远程服务调用失败: " + e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", errors);
        return ApiResponse.error(400, "参数验证失败: " + errors);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBindException(BindException e) {
        String errors = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Bind error: {}", errors);
        return ApiResponse.error(400, "参数绑定失败: " + errors);
    }

    /**
     * 处理IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: ", e);
        return ApiResponse.error(400, "参数错误: " + e.getMessage());
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("Unexpected error: ", e);
        return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
    }
}
