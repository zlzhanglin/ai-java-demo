package com.example.ai.service;

import com.example.ai.config.DashScopeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * AI服务实现
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final WebClient dashScopeWebClient;
    private final DashScopeProperties dashScopeProperties;

    /**
     * 发送问题并获取回答
     *
     * @param question 用户问题
     * @return AI回答
     */
    public String askQuestion(String question) {
        return askQuestion(question, dashScopeProperties.getDefaultModel());
    }

    /**
     * 发送问题并获取回答（指定模型）
     *
     * @param question 用户问题
     * @param model 模型名称
     * @return AI回答
     */
    public String askQuestion(String question, String model) {
        log.debug("Sending question to DashScope: {}", question);

        String requestBody = String.format(
                "{\"model\": \"%s\", \"input\": {\"prompt\": \"%s\"}}",
                model, escapeJson(question)
        );

        try {
            String response = dashScopeWebClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.debug("Received response from DashScope: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error calling DashScope API", e);
            throw new RuntimeException("Failed to get AI response: " + e.getMessage(), e);
        }
    }

    /**
     * 异步发送问题并获取回答
     *
     * @param question 用户问题
     * @return AI回答的Mono
     */
    public Mono<String> askQuestionAsync(String question) {
        return askQuestionAsync(question, dashScopeProperties.getDefaultModel());
    }

    /**
     * 异步发送问题并获取回答（指定模型）
     *
     * @param question 用户问题
     * @param model 模型名称
     * @return AI回答的Mono
     */
    public Mono<String> askQuestionAsync(String question, String model) {
        log.debug("Async sending question to DashScope: {}", question);

        String requestBody = String.format(
                "{\"model\": \"%s\", \"input\": {\"prompt\": \"%s\"}}",
                model, escapeJson(question)
        );

        return dashScopeWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.debug("Received async response from DashScope"))
                .doOnError(e -> log.error("Error calling DashScope API async", e));
    }

    /**
     * 转义JSON字符串
     *
     * @param value 原始字符串
     * @return 转义后的字符串
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
