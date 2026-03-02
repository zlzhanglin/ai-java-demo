package com.example.ai.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * DashScope Agent配置类
 *
 * @author linzhang
 * @since 2026/2/9
 */
@Configuration
public class DashScopeAgentConfig {

    @Bean
    public DashScopeAgentApi dashScopeAgentApi(
            @Value("${spring.ai.dashscope.base-url:https://dashscope.aliyuncs.com/api/v1}") String baseUrl,
            @Value("${spring.ai.dashscope.api-key}") String apiKey) {
        
        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json");
        
        RestClientAdapter adapter = RestClientAdapter.create((RestClient) restClientBuilder);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        
        return factory.createClient(DashScopeAgentApi.class);
    }
}
