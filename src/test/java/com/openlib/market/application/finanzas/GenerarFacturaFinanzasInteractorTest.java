package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class GenerarFacturaFinanzasInteractorTest {

    private IFacturacionGateway gateway;
    private GenerarFacturaFinanzasInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IFacturacionGateway.class);
        interactor = new GenerarFacturaFinanzasInteractor(gateway);
    }

    @Test
    void debeGenerarFacturaYGuardarlaExitosamente() {
        GenerarFacturaRequestDto req = new GenerarFacturaRequestDto(
                "p1", "v1", "123", "Razon Vendedor", "c1", "Juan", "juan@mail.com", 100.0
        );

        interactor.generarFactura(req);

        verify(gateway).guardarFactura(any(FacturaTributaria.class));
    }
}
