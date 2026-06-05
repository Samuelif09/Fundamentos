package com.openlib.market.frontend.http;

/**
 * Wrapper de respuesta HTTP con manejo unificado de éxito y error.
 */
public class ApiResponse<T> {
    private final int statusCode;
    private final T body;
    private final String errorMessage;
    private final boolean success;

    private ApiResponse(int statusCode, T body, String errorMessage, boolean success) {
        this.statusCode   = statusCode;
        this.body         = body;
        this.errorMessage = errorMessage;
        this.success      = success;
    }

    public static <T> ApiResponse<T> ok(int statusCode, T body) {
        return new ApiResponse<>(statusCode, body, null, true);
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return new ApiResponse<>(statusCode, null, message, false);
    }

    public boolean isSuccess()      { return success; }
    public int getStatusCode()      { return statusCode; }
    public T getBody()              { return body; }
    public String getErrorMessage() { return errorMessage; }
}
