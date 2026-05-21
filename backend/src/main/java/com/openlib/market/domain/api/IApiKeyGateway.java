package com.openlib.market.domain.api;

import java.util.Optional;

public interface IApiKeyGateway {
    void guardar(CredencialApi credencial);
    Optional<CredencialApi> buscarPorLlave(String valorLlave);
    Optional<CredencialApi> buscarPorId(String id);
}
