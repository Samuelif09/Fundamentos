package com.openlib.market.application.api;

import com.openlib.market.domain.api.CredencialApi;
import com.openlib.market.domain.api.IApiKeyGateway;

import org.springframework.stereotype.Service;

@Service
public class GenerarCredencialesApiInteractor implements IGenerarCredencialesApiUseCase {

    private final IApiKeyGateway apiKeyGateway;

    public GenerarCredencialesApiInteractor(IApiKeyGateway apiKeyGateway) {
        this.apiKeyGateway = apiKeyGateway;
    }

    @Override
    public CredencialApiDto generarCredencial(String idPropietario, String nombreApp) {
        CredencialApi credencial = new CredencialApi(idPropietario, nombreApp);
        apiKeyGateway.guardar(credencial);
        return toDto(credencial);
    }

    @Override
    public CredencialApiDto revocarCredencial(String id) {
        CredencialApi credencial = apiKeyGateway.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Credencial no encontrada"));

        credencial.revocar();
        apiKeyGateway.guardar(credencial);
        
        return toDto(credencial);
    }

    private CredencialApiDto toDto(CredencialApi c) {
        return new CredencialApiDto(
                c.getId(),
                c.getIdPropietario(),
                c.getNombreApp(),
                c.getLlave().valor(),
                c.getEstado().name()
        );
    }
}
