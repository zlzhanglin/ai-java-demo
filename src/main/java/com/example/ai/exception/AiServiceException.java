package com.example.ai.exception;

/**
 * AI服务异常
 *
 * @author linzhang
 * @since 2026/1/28
 */
public class AiServiceException extends RuntimeException {

    public AiServiceException(String message) {
        super(message);
    }

    public AiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
