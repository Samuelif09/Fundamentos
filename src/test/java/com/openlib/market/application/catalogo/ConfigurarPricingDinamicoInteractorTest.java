package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.*;
import com.openlib.market.domain.detalle.Isbn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ConfigurarPricingDinamicoInteractorTest {

    private IReglaPricingGateway gateway;
    private ConfigurarPricingDinamicoInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IReglaPricingGateway.class);
        interactor = new ConfigurarPricingDinamicoInteractor(gateway);
    }

    @Test
    void debeConfigurarPricingExitosamente() {
        ConfigurarPricingRequestDto req = new ConfigurarPricingRequestDto(
                "isbn-1", "v1", 10.0, 20.0, EstrategiaCompetencia.IGUALAR_MAS_BAJO
        );

        interactor.configurar(req);

        verify(gateway).guardar(any(ReglaPricing.class));
    }

    @Test
    void debeLanzarExcepcionSiMinimoEsMayorAMaximo() {
        ConfigurarPricingRequestDto req = new ConfigurarPricingRequestDto(
                "isbn-1", "v1", 20.0, 10.0, EstrategiaCompetencia.IGUALAR_MAS_BAJO
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.configurar(req));
        verifyNoInteractions(gateway);
    }
}
