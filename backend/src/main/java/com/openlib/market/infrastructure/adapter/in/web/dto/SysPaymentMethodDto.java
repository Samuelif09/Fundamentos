package com.openlib.market.infrastructure.adapter.in.web.dto;

public class SysPaymentMethodDto {
    private String id;
    private String name;
    private String provider;
    private String status;

    public SysPaymentMethodDto() {}

    public SysPaymentMethodDto(String id, String name, String provider, String status) {
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
