package com.openlib.market.domain.autenticacion;

public interface ITokenGeneratorGateway {
    TokenAcceso generar(UsuarioAuth usuario);
}
