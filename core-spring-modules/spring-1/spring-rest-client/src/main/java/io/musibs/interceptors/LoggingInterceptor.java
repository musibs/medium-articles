package io.musibs.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        
        // Log request
        logRequest(request, body);
        
        // Execute request and capture timing
        long startTime = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - startTime;
        
        // Log response
        logResponse(response, duration);
        
        return response;
    }
    
    private void logRequest(HttpRequest request, byte[] body) {
        log.info("=== HTTP Request ===");
        log.info("URI: {}", request.getURI());
        log.info("Method: {}", request.getMethod());
        log.info("Headers: {}", request.getHeaders());
        if (body.length > 0) {
            log.info("Body: {}", new String(body, StandardCharsets.UTF_8));
        }
    }
    
    private void logResponse(ClientHttpResponse response, long duration) throws IOException {
        log.info("=== HTTP Response ===");
        log.info("Status: {}", response.getStatusCode());
        log.info("Duration: {}ms", duration);
        log.info("Headers: {}", response.getHeaders());
    }
}