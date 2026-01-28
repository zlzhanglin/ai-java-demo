package com.example.ai.service;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 百炼Agent服务实现
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Slf4j
@Service
public class BailianAgentService {

    private final DashScopeAgent agent;
    private final String appId;

    public BailianAgentService(DashScopeAgentApi dashscopeAgentApi,
                               @Value("${spring.ai.dashscope.agent.app-id}") String appId) {
        this.appId = appId;
        this.agent = new DashScopeAgent(dashscopeAgentApi,
                DashScopeAgentOptions.builder()
                        .withSessionId("current_session_id")
                        .withIncrementalOutput(true)
                        .withHasThoughts(true)
                        .build());
    }

    /**
     * 流式调用Agent
     *
     * @param message 用户消息
     * @return 响应流
     */
    public Flux<String> streamAgent(String message) {
        log.debug("Streaming agent with message: {}", message);

        return agent.stream(new Prompt(message, DashScopeAgentOptions.builder().withAppId(appId).build()))
                .map(response -> {
                    if (response == null || response.getResult() == null) {
                        log.error("Chat response is null");
                        return "chat response is null";
                    }

                    AssistantMessage appOutput = response.getResult().getOutput();
                    String content = appOutput.getText();

                    DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput output =
                            (DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput)
                                    appOutput.getMetadata().get("output");

                    if (output != null) {
                        List<DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput.DashScopeAgentResponseOutputDocReference> docReferences = output.docReferences();
                        List<DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput.DashScopeAgentResponseOutputThoughts> thoughts = output.thoughts();

                        if (docReferences != null && !docReferences.isEmpty()) {
                            log.debug("Document references: {}", docReferences);
                        }

                        if (thoughts != null && !thoughts.isEmpty()) {
                            log.debug("Thoughts: {}", thoughts);
                        }
                    }

                    log.info("Agent response content:\n{}\n", content);
                    return content;
                })
                .doOnError(e -> log.error("Error in agent stream", e));
    }

    /**
     * 同步调用Agent
     *
     * @param message 用户消息
     * @return AI回答
     */
    public String callAgent(String message) {
        log.debug("Calling agent with message: {}", message);

        ChatResponse response = agent.call(new Prompt(message,
                DashScopeAgentOptions.builder().withAppId(appId).build()));

        if (response == null || response.getResult() == null) {
            log.error("Chat response is null");
            return "chat response is null";
        }

        AssistantMessage appOutput = response.getResult().getOutput();
        String content = appOutput.getText();

        DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput output =
                (DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput)
                        appOutput.getMetadata().get("output");

        if (output != null) {
            List<DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput.DashScopeAgentResponseOutputDocReference> docReferences = output.docReferences();
            List<DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput.DashScopeAgentResponseOutputThoughts> thoughts = output.thoughts();

            if (docReferences != null && !docReferences.isEmpty()) {
                for (DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput.DashScopeAgentResponseOutputDocReference docReference : docReferences) {
                    log.info("Document reference: {}\n", docReference);
                }
            }

            if (thoughts != null && !thoughts.isEmpty()) {
                for (DashScopeAgentApi.DashScopeAgentResponse.DashScopeAgentResponseOutput.DashScopeAgentResponseOutputThoughts thought : thoughts) {
                    log.info("Thought: {}\n", thought);
                }
            }
        }

        log.info("Agent response content:\n{}\n", content);
        return content;
    }
}
