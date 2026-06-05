package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerRegistrationRequest {
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("razonSocial")
    private String businessName;
    
    @JsonProperty("identificacionTributaria")
    private String taxId;

    public SellerRegistrationRequest() {}

    public SellerRegistrationRequest(String email, String password, String businessName, String taxId) {
        this.email = email;
        this.password = password;
        this.businessName = businessName;
        this.taxId = taxId;
    }

    @JsonProperty("nombre")
    public String getNombre() { return businessName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
}
