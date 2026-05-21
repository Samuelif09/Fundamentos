package com.openlib.market.infrastructure.adapter.in.web.dto;

public class SysCommissionDto {
    private double platformFeePercentage;

    public SysCommissionDto() {}

    public SysCommissionDto(double platformFeePercentage) {
        this.platformFeePercentage = platformFeePercentage;
    }

    public double getPlatformFeePercentage() { return platformFeePercentage; }
    public void setPlatformFeePercentage(double platformFeePercentage) { this.platformFeePercentage = platformFeePercentage; }
}
