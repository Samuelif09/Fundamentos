package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutRequest {
    private String fullName;
    private String address;
    private String city;
    private String zipCode;
    private String paymentMethod;

    public CheckoutRequest() {}

    public CheckoutRequest(String fullName, String address, String city, String zipCode, String paymentMethod) {
        this.fullName = fullName;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.paymentMethod = paymentMethod;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
