package com.openlib.market.domain.registro;

public class MotivoSuspension {
    private final String razon;

    public MotivoSuspension(String razon) {
        if (razon == null || razon.trim().isEmpty()) {
            throw new IllegalArgumentException("La razón de suspensión es obligatoria");
        }
        this.razon = razon;
    }

    public String getRazon() {
        return razon;
    }
}
