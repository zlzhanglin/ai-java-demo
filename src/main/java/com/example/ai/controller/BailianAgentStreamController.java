package com.example.ai.controller;

import com.example.ai.dto.response.ApiResponse;
import com.example.ai.service.BailianAgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 百炼Agent Controller
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Slf4j
@RestController
@RequestMapping("/api/ai/bailian")
@RequiredArgsConstructor
public class BailianAgentStreamController {

    private final BailianAgentService bailianAgentService;

    /**
     * 流式调用Agent接口
     *
     * @param message 用户消息
     * @return 响应流
     */
    @GetMapping(value = "/agent/stream", produces = "text/event-stream")
    public Flux<String> stream(@RequestParam(value = "message",
            defaultValue = "你好，请问你的知识库文档主要是关于什么内容的?") String message) {
        log.info("Received stream request with message: {}", message);
        return bailianAgentService.streamAgent(message);
    }

    /**
     * 同步调用Agent接口
     *
     * @param message 用户消息
     * @return API响应
     */
    @GetMapping("/agent/call")
    public ApiResponse<String> call(@RequestParam(value = "message",
            defaultValue = "如何使用SDK快速调用阿里云百炼的应用?") String message) {
        log.info("Received call request with message: {}", message);
        String response = bailianAgentService.callAgent(message);
        return ApiResponse.success(response);
    }
}