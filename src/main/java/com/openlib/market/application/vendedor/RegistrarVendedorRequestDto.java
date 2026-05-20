package com.openlib.market.application.vendedor;

public class RegistrarVendedorRequestDto {
    private final String nombre;
    private final String email;
    private final String password;
    private final String razonSocial;
    private final String identificacionTributaria;

    public RegistrarVendedorRequestDto(String nombre, String email, String password, String razonSocial, String identificacionTributaria) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.razonSocial = razonSocial;
        this.identificacionTributaria = identificacionTributaria;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRazonSocial() { return razonSocial; }
    public String getIdentificacionTributaria() { return identificacionTributaria; }
}
