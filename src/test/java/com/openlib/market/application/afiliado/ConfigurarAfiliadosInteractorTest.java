package com.openlib.market.application.afiliado;

import com.openlib.market.domain.afiliado.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ConfigurarAfiliadosInteractorTest {

    private IAfiliadoGateway gateway;
    private ConfigurarAfiliadosInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IAfiliadoGateway.class);
        interactor = new ConfigurarAfiliadosInteractor(gateway);
    }

    @Test
    void debeConfigurarAfiliadoYGenerarUrlExitosamente() {
        when(gateway.obtenerProgramaPorVendedor("v1")).thenReturn(Optional.empty());

        ConfigurarAfiliadosRequestDto req = new ConfigurarAfiliadosRequestDto("v1", "a1", 50.0);
        String url = interactor.configurarYGenerarEnlace(req);

        assertTrue(url.startsWith("https://openlib.market/vendedor/v1?ref="));
        verify(gateway).guardarPrograma(any(ProgramaAfiliado.class));
        verify(gateway).guardarEnlace(any(EnlaceAfiliado.class));
    }

    @Test
    void debeReutilizarProgramaExistente() {
        ProgramaAfiliado programaExistente = new ProgramaAfiliado("v1", new PorcentajeComisionAfiliado(10.0));
        when(gateway.obtenerProgramaPorVendedor("v1")).thenReturn(Optional.of(programaExistente));

        ConfigurarAfiliadosRequestDto req = new ConfigurarAfiliadosRequestDto("v1", "a1", 10.0);
        interactor.configurarYGenerarEnlace(req);

        verify(gateway, never()).guardarPrograma(any(ProgramaAfiliado.class));
        verify(gateway).guardarEnlace(any(EnlaceAfiliado.class));
    }
}
