package com.openlib.market.infrastructure.api;

import com.openlib.market.domain.api.CredencialApi;
import com.openlib.market.domain.api.EstadoLlave;
import com.openlib.market.domain.api.IApiKeyGateway;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

import org.springframework.context.annotation.Lazy;

@Component
public class ApiKeySecurityInterceptor implements HandlerInterceptor {

    private final IApiKeyGateway apiKeyGateway;

    public ApiKeySecurityInterceptor(@Lazy IApiKeyGateway apiKeyGateway) {
        this.apiKeyGateway = apiKeyGateway;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Solo protegemos las rutas públicas de integración que definamos. 
        // Para este MVP, supongamos que cualquier ruta bajo /api/v1/public/ requiere API Key.
        if (request.getRequestURI().startsWith("/api/v1/public/")) {
            String apiKeyHeader = request.getHeader("X-API-KEY");
            
            if (apiKeyHeader == null || apiKeyHeader.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("API Key missing");
                return false;
            }
            
            Optional<CredencialApi> credencial = apiKeyGateway.buscarPorLlave(apiKeyHeader);
            if (credencial.isEmpty() || credencial.get().getEstado() == EstadoLlave.REVOCADA) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid or revoked API Key");
                return false;
            }
        }
        
        return true;
    }
}
