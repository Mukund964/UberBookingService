package org.example.uberbookingservice.config;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class RequestTraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. Generate a unique ID for this request
        String traceId = UUID.randomUUID().toString();

        // 2. Put it into the "MDC" map.
        MDC.put("traceId", traceId);

        try {
            // 3. Continue to the Controller
            chain.doFilter(request, response);
        } finally {
            // 4. Clean up
            MDC.clear();
        }
    }
}