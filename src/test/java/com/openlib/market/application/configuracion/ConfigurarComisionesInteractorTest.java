package com.openlib.market.application.configuracion;

import com.openlib.market.domain.configuracion.IConfiguracionComisionGateway;
import com.openlib.market.domain.configuracion.ReglaComision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurarComisionesInteractorTest {

    private IConfiguracionComisionGateway comisionGateway;
    private ConfigurarComisionesInteractor interactor;

    @BeforeEach
    void setUp() {
        comisionGateway = mock(IConfiguracionComisionGateway.class);
        interactor = new ConfigurarComisionesInteractor(comisionGateway);
    }

    @Test
    void debeConfigurarComision() {
        interactor.configurarComision("FICCION", 15.0);
        verify(comisionGateway).guardarRegla(any(ReglaComision.class));
    }

    @Test
    void debeDevolverComisionEspecificaSiExiste() {
        when(comisionGateway.obtenerRegla("FICCION")).thenReturn(new ReglaComision("FICCION", 15.0));

        ComisionDto dto = interactor.obtenerComisionParaCategoria("FICCION");

        assertEquals("FICCION", dto.getIdCategoria());
        assertEquals(15.0, dto.getPorcentajeComision());
    }

    @Test
    void debeRetrocederAGlobalSiNoExisteEspecifica() {
        when(comisionGateway.obtenerRegla("CIENCIA")).thenReturn(null);
        when(comisionGateway.obtenerRegla("GLOBAL")).thenReturn(new ReglaComision("GLOBAL", 10.0));

        ComisionDto dto = interactor.obtenerComisionParaCategoria("CIENCIA");

        assertEquals("GLOBAL", dto.getIdCategoria());
        assertEquals(10.0, dto.getPorcentajeComision());
    }

    @Test
    void debeLanzarExcepcionSiNoHayGlobal() {
        when(comisionGateway.obtenerRegla("CIENCIA")).thenReturn(null);
        when(comisionGateway.obtenerRegla("GLOBAL")).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> 
            interactor.obtenerComisionParaCategoria("CIENCIA")
        );
    }
}
