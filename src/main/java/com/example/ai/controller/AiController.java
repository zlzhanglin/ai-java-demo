package com.example.ai.controller;

import com.example.ai.dto.request.AiQuestionRequest;
import com.example.ai.dto.response.ApiResponse;
import com.example.ai.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * AI问答Controller
 *
 * @author linzhang
 * @since 2026/1/27 23:04
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * 同步问答接口（兼容旧版）
     *
     * @param question 用户问题
     * @return AI回答
     */
    @GetMapping("/ask")
    public ApiResponse<String> ask(@RequestParam String question) {
        log.info("Received question: {}", question);
        String answer = aiService.askQuestion(question);
        return ApiResponse.success(answer);
    }

    /**
     * 同步问答接口（POST）
     *
     * @param request 请求对象
     * @return AI回答
     */
    @PostMapping("/ask")
    public ApiResponse<String> askPost(@Valid @RequestBody AiQuestionRequest request) {
        log.info("Received question: {}", request.getQuestion());
        
        String answer;
        if (StringUtils.hasText(request.getModel())) {
            answer = aiService.askQuestion(request.getQuestion(), request.getModel());
        } else {
            answer = aiService.askQuestion(request.getQuestion());
        }
        
        return ApiResponse.success(answer);
    }

    /**
     * 异步问答接口
     *
     * @param request 请求对象
     * @return AI回答的Mono
     */
    @PostMapping("/ask/async")
    public Mono<ApiResponse<String>> askAsync(@Valid @RequestBody AiQuestionRequest request) {
        log.info("Received async question: {}", request.getQuestion());
        
        Mono<String> answerMono;
        if (StringUtils.hasText(request.getModel())) {
            answerMono = aiService.askQuestionAsync(request.getQuestion(), request.getModel());
        } else {
            answerMono = aiService.askQuestionAsync(request.getQuestion());
        }
        
        return answerMono.map(ApiResponse::success);
    }
}