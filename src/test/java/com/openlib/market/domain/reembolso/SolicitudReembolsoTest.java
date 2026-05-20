package com.openlib.market.domain.reembolso;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudReembolsoTest {

    @Test
    void debeCrearSolicitudSiMontoEsValido() {
        Pedido pedido = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        SolicitudReembolso solicitud = new SolicitudReembolso("p1", 50.0, "Libro defectuoso", pedido);
        
        assertEquals(EstadoReembolso.PENDIENTE, solicitud.getEstado());
        assertEquals(50.0, solicitud.getMontoReembolso());
    }

    @Test
    void debeLanzarExcepcionSiMontoSuperaTotalPedido() {
        Pedido pedido = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        
        assertThrows(MontoReembolsoInvalidoException.class, () -> 
            new SolicitudReembolso("p1", 150.0, "Libro defectuoso", pedido)
        );
    }

    @Test
    void debeAprobarSolicitudPendiente() {
        Pedido pedido = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        SolicitudReembolso solicitud = new SolicitudReembolso("p1", 50.0, "Libro defectuoso", pedido);
        
        solicitud.aprobar();
        assertEquals(EstadoReembolso.APROBADO, solicitud.getEstado());
    }
}
