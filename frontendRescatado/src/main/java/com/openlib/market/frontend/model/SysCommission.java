package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysCommission {
    private double platformFeePercentage;

    public SysCommission() {}
    public SysCommission(double platformFeePercentage) {
        this.platformFeePercentage = platformFeePercentage;
    }

    public double getPlatformFeePercentage() { return platformFeePercentage; }
    public void setPlatformFeePercentage(double platformFeePercentage) { this.platformFeePercentage = platformFeePercentage; }
}
