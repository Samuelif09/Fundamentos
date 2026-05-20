package com.openlib.market.domain.autenticacion;

import java.util.Optional;

public interface IUsuarioAuthGateway {
    Optional<UsuarioAuth> buscarPorEmail(Email email);
}
