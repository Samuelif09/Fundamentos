package com.openlib.market.domain.registro;

public interface IRegistroGateway {
    void guardar(Usuario usuario);
    boolean existeEmail(Email email);
}
