package com.openlib.market.application.reembolso;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import com.openlib.market.domain.reembolso.EstadoReembolso;
import com.openlib.market.domain.reembolso.IPasarelaPagoGateway;
import com.openlib.market.domain.reembolso.IReembolsoGateway;
import com.openlib.market.domain.reembolso.MontoReembolsoInvalidoException;
import com.openlib.market.domain.reembolso.SolicitudReembolso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionarReembolsosInteractorTest {

    private IReembolsoGateway reembolsoGateway;
    private IPedidoGateway pedidoGateway;
    private IPasarelaPagoGateway pasarelaPagoGateway;
    private GestionarReembolsosInteractor interactor;

    @BeforeEach
    void setUp() {
        reembolsoGateway = mock(IReembolsoGateway.class);
        pedidoGateway = mock(IPedidoGateway.class);
        pasarelaPagoGateway = mock(IPasarelaPagoGateway.class);
        interactor = new GestionarReembolsosInteractor(reembolsoGateway, pedidoGateway, pasarelaPagoGateway);
    }

    @Test
    void debeCrearSolicitudDeReembolso() {
        Pedido pedido = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        when(pedidoGateway.obtenerPorId("p1")).thenReturn(Optional.of(pedido));

        ReembolsoDto dto = interactor.solicitarReembolso("p1", 100.0, "Cancelación");

        assertEquals("PENDIENTE", dto.getEstado());
        verify(reembolsoGateway).guardar(any(SolicitudReembolso.class));
    }

    @Test
    void debeRechazarSolicitudSiMontoExcede() {
        Pedido pedido = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        when(pedidoGateway.obtenerPorId("p1")).thenReturn(Optional.of(pedido));

        assertThrows(MontoReembolsoInvalidoException.class, () -> 
            interactor.solicitarReembolso("p1", 150.0, "Fraude")
        );
    }

    @Test
    void debeAprobarReembolsoLlamandoPasarela() {
        SolicitudReembolso solicitud = new SolicitudReembolso("r1", "p1", 50.0, "Motivo", EstadoReembolso.PENDIENTE);
        when(reembolsoGateway.obtenerPorId("r1")).thenReturn(Optional.of(solicitud));
        when(pasarelaPagoGateway.ejecutarReembolso("p1", 50.0)).thenReturn(true);

        interactor.aprobarReembolso("r1");

        assertEquals(EstadoReembolso.APROBADO, solicitud.getEstado());
        verify(pasarelaPagoGateway).ejecutarReembolso("p1", 50.0);
        verify(reembolsoGateway).actualizar(solicitud);
    }
}
