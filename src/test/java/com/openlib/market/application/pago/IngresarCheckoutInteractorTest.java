package com.openlib.market.application.pago;

import com.openlib.market.domain.pago.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngresarCheckoutInteractorTest {

    private IPasarelaPagoGateway pasarelaPago;
    private IEventPublisher eventPublisher;
    private IngresarCheckoutInteractor interactor;

    @BeforeEach
    void setUp() {
        pasarelaPago = mock(IPasarelaPagoGateway.class);
        eventPublisher = mock(IEventPublisher.class);
        interactor = new IngresarCheckoutInteractor(pasarelaPago, eventPublisher);
    }

    @Test
    void debeLanzarExcepcionYNoPublicarEventoSiPagoEsRechazado() {
        when(pasarelaPago.procesarCobro(any(TokenPago.class), any(Monto.class))).thenReturn(false);

        CheckoutRequestDto req = new CheckoutRequestDto("user-1", "order-1", 100.0, "tok_invalid");

        assertThrows(PagoRechazadoException.class, () -> interactor.procesarCheckout(req));

        verify(eventPublisher, never()).publicar(any());
    }

    @Test
    void debeProcesarExitosamenteYPublicarEvento() {
        when(pasarelaPago.procesarCobro(any(TokenPago.class), any(Monto.class))).thenReturn(true);

        CheckoutRequestDto req = new CheckoutRequestDto("user-2", "order-2", 150.0, "tok_valid");

        assertDoesNotThrow(() -> interactor.procesarCheckout(req));

        verify(eventPublisher, times(1)).publicar(any(PedidoCompletadoEvent.class));
    }
}
