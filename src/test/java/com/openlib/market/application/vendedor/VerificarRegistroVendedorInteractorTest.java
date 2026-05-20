package com.openlib.market.application.vendedor;

import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import com.openlib.market.domain.vendedor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VerificarRegistroVendedorInteractorTest {

    private IVendedorGateway vendedorGateway;
    private IAlmacenamientoGateway almacenamientoGateway;
    private INotificacionAdminGateway notificacionAdminGateway;
    private VerificarRegistroVendedorInteractor interactor;

    @BeforeEach
    void setUp() {
        vendedorGateway = mock(IVendedorGateway.class);
        almacenamientoGateway = mock(IAlmacenamientoGateway.class);
        notificacionAdminGateway = mock(INotificacionAdminGateway.class);
        interactor = new VerificarRegistroVendedorInteractor(vendedorGateway, almacenamientoGateway, notificacionAdminGateway);
    }

    @Test
    void debeSolicitarVerificacionExitosamente() {
        Vendedor vendedor = new Vendedor("v1", "u1", new RazonSocial("R"), new IdentificacionTributaria("123456789"));
        when(vendedorGateway.obtenerPorId("v1")).thenReturn(Optional.of(vendedor));

        byte[] img = new byte[100];
        interactor.solicitarVerificacion("v1", img, "image/jpeg", "id.jpg");

        assertEquals(EstadoVerificacion.EN_REVISION, vendedor.getEstadoVerificacion());
        verify(almacenamientoGateway).guardar(any(ArchivoImagen.class), eq("verificacion_v1"));
        verify(vendedorGateway).actualizar(vendedor);
        verify(notificacionAdminGateway).notificarVerificacionPendiente("v1");
    }

    @Test
    void debeLanzarExcepcionSiYaEstaEnRevision() {
        Vendedor vendedor = new Vendedor("v1", "u1", new RazonSocial("R"), new IdentificacionTributaria("123456789"));
        vendedor.solicitarVerificacion();

        when(vendedorGateway.obtenerPorId("v1")).thenReturn(Optional.of(vendedor));

        byte[] img = new byte[100];
        assertThrows(VerificacionEnCursoException.class, () -> 
            interactor.solicitarVerificacion("v1", img, "image/jpeg", "id.jpg")
        );
    }
}
