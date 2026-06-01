package com.openlib.market.infrastructure.api;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MeResolveFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String uri = httpRequest.getRequestURI();
            if (uri != null && (uri.contains("/vendedores/me") || uri.contains("/usuarios/me"))) {
                String authHeader = httpRequest.getHeader("Authorization");
                String userId = null;
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    String[] parts = token.split("\\.");
                    if (parts.length > 1) {
                        userId = parts[1];
                    }
                }
                
                if (userId != null && !userId.trim().isEmpty()) {
                    final String targetUserId = userId;
                    String newUri = uri.replace("/vendedores/me", "/vendedores/" + targetUserId)
                                       .replace("/usuarios/me", "/usuarios/" + targetUserId);
                    HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                        @Override
                        public String getRequestURI() {
                            return newUri;
                        }

                        @Override
                        public StringBuffer getRequestURL() {
                            StringBuffer url = new StringBuffer();
                            String scheme = getScheme();
                            int port = getServerPort();
                            url.append(scheme).append("://").append(getServerName());
                            if (("http".equals(scheme) && port != 80) || ("https".equals(scheme) && port != 443)) {
                                url.append(":").append(port);
                            }
                            url.append(newUri);
                            return url;
                        }

                        @Override
                        public String getServletPath() {
                            String original = super.getServletPath();
                            if (original != null) {
                                if (original.contains("/vendedores/me")) {
                                    return original.replace("/vendedores/me", "/vendedores/" + targetUserId);
                                }
                                if (original.contains("/usuarios/me")) {
                                    return original.replace("/usuarios/me", "/usuarios/" + targetUserId);
                                }
                            }
                            return original;
                        }

                        @Override
                        public String getPathInfo() {
                            String original = super.getPathInfo();
                            if (original != null) {
                                if (original.contains("/vendedores/me")) {
                                    return original.replace("/vendedores/me", "/vendedores/" + targetUserId);
                                }
                                if (original.contains("/usuarios/me")) {
                                    return original.replace("/usuarios/me", "/usuarios/" + targetUserId);
                                }
                            }
                            return original;
                        }
                    };
                    chain.doFilter(wrappedRequest, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}
