package com.example.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应DTO
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .build();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(200)
                .message("success")
                .build();
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    /**
     * 失败响应（默认500）
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }
}
