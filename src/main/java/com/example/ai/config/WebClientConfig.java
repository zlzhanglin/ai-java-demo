package com.example.ai.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebClient配置
 *
 * @author linzhang
 * @since 2026/1/28
 */
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final DashScopeProperties dashScopeProperties;

    @Bean
    public WebClient dashScopeWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(dashScopeProperties.getTimeout()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(dashScopeProperties.getTimeout(), TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(dashScopeProperties.getTimeout(), TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(dashScopeProperties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + dashScopeProperties.getApiKey())
                .defaultHeader("Content-Type", "application/json")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
