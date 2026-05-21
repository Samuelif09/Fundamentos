package com.openlib.market.application.gestionUsuarios;

import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.INotificacionAdminGateway;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.EstadoVerificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AprobarGestionUsuariosInteractorTest {

    private IVendedorGateway vendedorGateway;
    private INotificacionAdminGateway notificacionGateway;
    private AprobarGestionUsuariosInteractor interactor;

    @BeforeEach
    void setUp() {
        vendedorGateway = mock(IVendedorGateway.class);
        notificacionGateway = mock(INotificacionAdminGateway.class);
        interactor = new AprobarGestionUsuariosInteractor(vendedorGateway, notificacionGateway);
    }

    @Test
    void debeAprobarVendedorEnRevision() {
        Vendedor vendedor = new Vendedor("seller1", "user1", new RazonSocial("Mi Tienda"), new IdentificacionTributaria("12345"), EstadoVerificacion.EN_REVISION);
        when(vendedorGateway.obtenerPorId("seller1")).thenReturn(Optional.of(vendedor));

        interactor.aprobarVendedor("seller1");

        assertEquals(EstadoVerificacion.APROBADO, vendedor.getEstadoVerificacion());
        verify(vendedorGateway, times(1)).actualizar(vendedor);
        verify(notificacionGateway, times(1)).notificarVendedorAprobado("seller1");
    }

    @Test
    void debeLanzarExcepcionSiVendedorNoExiste() {
        when(vendedorGateway.obtenerPorId("seller1")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> interactor.aprobarVendedor("seller1"));
        assertEquals("Vendedor no encontrado", ex.getMessage());
        verify(vendedorGateway, never()).actualizar(any());
    }
}
