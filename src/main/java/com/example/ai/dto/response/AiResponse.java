package com.example.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI响应DTO
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {

    /**
     * AI回答内容
     */
    private String answer;

    /**
     * 使用的模型
     */
    private String model;

    /**
     * 请求耗时（毫秒）
     */
    private Long duration;
}
