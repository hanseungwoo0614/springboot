package com.example.ksl.wsd2025.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 모든 API 응답을 같은 모양으로 감싸는 표준 응답 클래스
 *
 * 성공: { "status": "success", "data": { ... } }
 * 실패: { "status": "error", "message": "..." }
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // 값이 없는(null) 항목은 JSON에서 생략
public class ApiResponse<T> {

    private final String status;
    private final T data;
    private final String message;

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    // 성공 응답 (데이터만)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data, null);
    }

    // 성공 응답 (데이터 + 안내 메시지)
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("success", data, message);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", null, message);
    }

    public String getStatus() { return status; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}