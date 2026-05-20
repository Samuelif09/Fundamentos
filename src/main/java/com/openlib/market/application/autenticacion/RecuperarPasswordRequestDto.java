package com.openlib.market.application.autenticacion;

public class RecuperarPasswordRequestDto {
    private String email;

    public RecuperarPasswordRequestDto() {}

    public RecuperarPasswordRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
