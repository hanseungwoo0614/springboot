package com.example.ksl.wsd2025.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 미들웨어 1: 요청 로그 출력
 * 모든 /api/** 요청의 메소드, URL, 응답 코드, 처리 시간을 콘솔에 출력한다.
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청이 들어온 시간을 기록해 둔다
        request.setAttribute("startTime", System.currentTimeMillis());

        String query = request.getQueryString();
        System.out.println("[LoggingInterceptor] 요청 시작: " + request.getMethod() + " "
                + request.getRequestURI() + (query != null ? "?" + query : ""));
        return true; // true → 컨트롤러로 진행
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        long startTime = (long) request.getAttribute("startTime");
        long elapsed = System.currentTimeMillis() - startTime;

        System.out.println("[LoggingInterceptor] 요청 완료: " + request.getMethod() + " "
                + request.getRequestURI() + " → status=" + response.getStatus()
                + " (" + elapsed + "ms)");
    }
}