package com.openlib.market.application.configuracion;

import com.openlib.market.domain.configuracion.ConfiguracionInvalidaException;
import com.openlib.market.domain.configuracion.ConfiguracionMetodoPago;
import com.openlib.market.domain.configuracion.EstadoMetodoPago;
import com.openlib.market.domain.configuracion.IMetodoPagoConfigGateway;
import com.openlib.market.domain.configuracion.NombreMetodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionarConfiguracionSistemaInteractorTest {

    private IMetodoPagoConfigGateway configGateway;
    private GestionarConfiguracionSistemaInteractor interactor;

    @BeforeEach
    void setUp() {
        configGateway = mock(IMetodoPagoConfigGateway.class);
        interactor = new GestionarConfiguracionSistemaInteractor(configGateway);
    }

    @Test
    void debeListarMetodos() {
        ConfiguracionMetodoPago conf = new ConfiguracionMetodoPago("id1", new NombreMetodo("Stripe"), EstadoMetodoPago.HABILITADO);
        when(configGateway.listarTodos()).thenReturn(List.of(conf));

        List<MetodoPagoConfigDto> res = interactor.listarMetodosPago();
        assertEquals(1, res.size());
        assertEquals("Stripe", res.get(0).getNombre());
    }

    @Test
    void debeDeshabilitarMetodoSiHayVariosActivos() {
        ConfiguracionMetodoPago conf = new ConfiguracionMetodoPago("id1", new NombreMetodo("Stripe"), EstadoMetodoPago.HABILITADO);
        when(configGateway.obtenerPorId("id1")).thenReturn(Optional.of(conf));
        when(configGateway.contarMetodosHabilitados()).thenReturn(2);

        interactor.cambiarEstadoMetodoPago("id1", "DESHABILITADO");
        assertEquals(EstadoMetodoPago.DESHABILITADO, conf.getEstado());
        verify(configGateway).actualizar(conf);
    }

    @Test
    void debeFallarAlDeshabilitarSiEsElUnicoActivo() {
        ConfiguracionMetodoPago conf = new ConfiguracionMetodoPago("id1", new NombreMetodo("Stripe"), EstadoMetodoPago.HABILITADO);
        when(configGateway.obtenerPorId("id1")).thenReturn(Optional.of(conf));
        when(configGateway.contarMetodosHabilitados()).thenReturn(1);

        assertThrows(ConfiguracionInvalidaException.class, () -> {
            interactor.cambiarEstadoMetodoPago("id1", "DESHABILITADO");
        });
        // No debe haberse llamado actualizar
        verify(configGateway, never()).actualizar(any());
    }
}
