package com.openlib.market.application.marketing;

import com.openlib.market.domain.marketing.BannerPromocional;
import com.openlib.market.domain.marketing.EstadoCampana;
import com.openlib.market.domain.marketing.IBannerGateway;
import com.openlib.market.domain.marketing.PeriodoCampana;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionarBannersInteractorTest {

    private IBannerGateway bannerGateway;
    private GestionarBannersInteractor interactor;

    @BeforeEach
    void setUp() {
        bannerGateway = mock(IBannerGateway.class);
        interactor = new GestionarBannersInteractor(bannerGateway);
    }

    @Test
    void debeCrearBannerCorrectamente() {
        String inicio = LocalDateTime.now().minusDays(1).toString();
        String fin = LocalDateTime.now().plusDays(1).toString();

        BannerDto dto = interactor.crearBanner("Promo", "img", "url", inicio, fin);

        assertNotNull(dto.getId());
        assertTrue(dto.isVigente());
        verify(bannerGateway).guardar(any(BannerPromocional.class));
    }

    @Test
    void debeCambiarEstadoDeBanner() {
        PeriodoCampana periodo = new PeriodoCampana(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        BannerPromocional banner = new BannerPromocional("Promo", "img", "url", periodo);
        
        when(bannerGateway.obtenerPorId(banner.getId())).thenReturn(banner);

        interactor.cambiarEstado(banner.getId(), "INACTIVA");

        assertEquals(EstadoCampana.INACTIVA, banner.getEstado());
        verify(bannerGateway).actualizar(banner);
    }
}
