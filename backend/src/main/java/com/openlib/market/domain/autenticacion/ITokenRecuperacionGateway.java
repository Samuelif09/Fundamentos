package com.openlib.market.domain.autenticacion;

public interface ITokenRecuperacionGateway {
    void guardar(Email email, TokenRecuperacion token);
}
