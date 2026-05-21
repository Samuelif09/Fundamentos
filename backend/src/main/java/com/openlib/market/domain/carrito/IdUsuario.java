package com.openlib.market.domain.carrito;

public class IdUsuario {
    private final String id;

    public IdUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser vacío");
        }
        this.id = id;
    }

    public String getId() { return id; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdUsuario that = (IdUsuario) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() { return id.hashCode(); }
}
