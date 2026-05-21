package com.openlib.market.infrastructure.adapter.in.web.dto;

public class SysCategoryDto {
    private String id;
    private String name;

    public SysCategoryDto() {}

    public SysCategoryDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
