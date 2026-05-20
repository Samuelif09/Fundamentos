package com.openlib.market.domain.autenticacion;

public interface IEmailGateway {
    void enviarTokenRecuperacion(Email email, TokenRecuperacion token);
}
