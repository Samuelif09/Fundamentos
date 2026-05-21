package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse {
    private String message;
    private boolean requiresAdminValidation;

    public RegistrationResponse() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRequiresAdminValidation() { return requiresAdminValidation; }
    public void setRequiresAdminValidation(boolean requiresAdminValidation) { this.requiresAdminValidation = requiresAdminValidation; }
}
