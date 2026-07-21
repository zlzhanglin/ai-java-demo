package com.example.ai.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * AI问答请求DTO
 *
 * @author lin zhang
 * @since 2026/1/28
 */
@Data
public class AiQuestionRequest {

    /**
     * 用户问题
     */
    @NotBlank(message = "问题不能为空")
    private String question;

    /**
     * 模型名称（可选）
     */
    private String model;
}
