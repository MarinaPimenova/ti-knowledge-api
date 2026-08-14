package com.wk.ti.observability;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingContextFilter implements Filter {
    private static final String HEADER_NAME = "X-Request-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // Extract from headers or security context
            String requestId = httpRequest.getHeader(HEADER_NAME);

            if (requestId != null) MDC.put("requestId", requestId);

            chain.doFilter(request, response);
        } finally {
            // Clean up MDC after request finishes to avoid context leaks across thread pools
            MDC.remove("requestId");
        }
    }
}

