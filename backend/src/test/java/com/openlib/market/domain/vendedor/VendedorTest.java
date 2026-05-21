package com.openlib.market.domain.vendedor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VendedorTest {

    @Test
    void debeCrearVendedorEnEstadoNoIniciado() {
        Vendedor vendedor = new Vendedor("user123", new RazonSocial("Mi Tienda"), new IdentificacionTributaria("123456789"));
        assertEquals(EstadoVerificacion.NO_INICIADO, vendedor.getEstadoVerificacion());
    }

    @Test
    void debeCambiarEstadoAEnRevisionAlSolicitarVerificacion() {
        Vendedor vendedor = new Vendedor("user123", new RazonSocial("Mi Tienda"), new IdentificacionTributaria("123456789"));
        vendedor.solicitarVerificacion();
        assertEquals(EstadoVerificacion.EN_REVISION, vendedor.getEstadoVerificacion());
    }

    @Test
    void debeAprobarVendedorEnRevision() {
        Vendedor vendedor = new Vendedor("user123", new RazonSocial("Mi Tienda"), new IdentificacionTributaria("123456789"));
        vendedor.solicitarVerificacion(); // Pasa a EN_REVISION
        vendedor.aprobar();
        assertEquals(EstadoVerificacion.APROBADO, vendedor.getEstadoVerificacion());
    }

    @Test
    void noDebeAprobarVendedorQueNoEstaEnRevision() {
        Vendedor vendedor = new Vendedor("user123", new RazonSocial("Mi Tienda"), new IdentificacionTributaria("123456789"));
        // Estado inicial es NO_INICIADO
        assertThrows(SolicitudInvalidaException.class, vendedor::aprobar);
    }
}
