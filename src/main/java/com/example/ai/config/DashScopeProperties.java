package com.example.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DashScope配置属性
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Data
@Component
@ConfigurationProperties(prefix = "dashscope")
public class DashScopeProperties {

    /**
     * DashScope API基础URL
     */
    private String baseUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 默认模型
     */
    private String defaultModel = "qwen-max";

    /**
     * 请求超时时间（秒）
     */
    private int timeout = 60;
}
