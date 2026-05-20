package com.openlib.market.application.api;

public interface IGenerarCredencialesApiUseCase {
    CredencialApiDto generarCredencial(String idPropietario, String nombreApp);
    CredencialApiDto revocarCredencial(String id);
}
