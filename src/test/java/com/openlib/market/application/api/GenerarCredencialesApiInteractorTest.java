package com.openlib.market.application.api;

import com.openlib.market.domain.api.CredencialApi;
import com.openlib.market.domain.api.EstadoLlave;
import com.openlib.market.domain.api.IApiKeyGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerarCredencialesApiInteractorTest {

    private IApiKeyGateway apiKeyGateway;
    private GenerarCredencialesApiInteractor interactor;

    @BeforeEach
    void setUp() {
        apiKeyGateway = mock(IApiKeyGateway.class);
        interactor = new GenerarCredencialesApiInteractor(apiKeyGateway);
    }

    @Test
    void debeGenerarGuardarYRetornarCredencial() {
        CredencialApiDto dto = interactor.generarCredencial("admin1", "App Externa");

        assertNotNull(dto.getId());
        assertEquals("admin1", dto.getIdPropietario());
        assertEquals("ACTIVA", dto.getEstado());
        verify(apiKeyGateway).guardar(any(CredencialApi.class));
    }

    @Test
    void debeRevocarCredencialYGuardar() {
        CredencialApi cred = new CredencialApi("admin1", "App Externa");
        when(apiKeyGateway.buscarPorId(cred.getId())).thenReturn(Optional.of(cred));

        CredencialApiDto dto = interactor.revocarCredencial(cred.getId());

        assertEquals("REVOCADA", dto.getEstado());
        verify(apiKeyGateway).guardar(cred);
    }
}
