package com.openlib.market.domain.autenticacion;

public interface IVerificadorPasswordGateway {
    boolean verificar(PasswordPlano passwordPlano, String hashAlmacenado);
}
