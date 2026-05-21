package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SolicitarRetiroFinanzasInteractorTest {

    private IBilleteraGateway billeteraGateway;
    private IRetiroGateway retiroGateway;
    private SolicitarRetiroFinanzasInteractor interactor;

    @BeforeEach
    void setUp() {
        billeteraGateway = mock(IBilleteraGateway.class);
        retiroGateway = mock(IRetiroGateway.class);
        interactor = new SolicitarRetiroFinanzasInteractor(billeteraGateway, retiroGateway);
    }

    @Test
    void debeCrearSolicitudYReducirSaldoExitosamente() {
        BilleteraVendedor billetera = new BilleteraVendedor("seller-1", 100.0);
        when(billeteraGateway.obtenerPorIdVendedor("seller-1")).thenReturn(Optional.of(billetera));

        interactor.solicitarRetiro("seller-1", 50.0, "CBU-123456");

        verify(billeteraGateway).guardar(argThat(b -> b.getSaldoDisponible() == 50.0));
        verify(retiroGateway).guardar(argThat(s -> s.getEstado() == EstadoRetiro.PENDIENTE));
    }

    @Test
    void debeLanzarExcepcionSiFondosInsuficientes() {
        BilleteraVendedor billetera = new BilleteraVendedor("seller-1", 100.0);
        when(billeteraGateway.obtenerPorIdVendedor("seller-1")).thenReturn(Optional.of(billetera));

        assertThrows(FondosInsuficientesException.class, () ->
                interactor.solicitarRetiro("seller-1", 150.0, "CBU-123456")
        );

        // Nunca debe persistir si hay error de fondos
        verify(billeteraGateway, never()).guardar(any());
        verify(retiroGateway, never()).guardar(any());
    }

    @Test
    void debeCrearBilleteraConSaldoCeroSiNoExiste() {
        when(billeteraGateway.obtenerPorIdVendedor("seller-new")).thenReturn(Optional.empty());

        assertThrows(FondosInsuficientesException.class, () ->
                interactor.solicitarRetiro("seller-new", 10.0, "CBU-999")
        );
    }
}
