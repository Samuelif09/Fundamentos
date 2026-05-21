package com.openlib.market.domain.curaduria;

public class MotivoRechazo {
    private final String razon;

    public MotivoRechazo(String razon) {
        if (razon == null || razon.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de rechazo es obligatorio.");
        }
        if (razon.trim().length() < 10) {
            throw new IllegalArgumentException("El motivo de rechazo debe tener al menos 10 caracteres.");
        }
        this.razon = razon.trim();
    }

    public String getRazon() {
        return razon;
    }
}
