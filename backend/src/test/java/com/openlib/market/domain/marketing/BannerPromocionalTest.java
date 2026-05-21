package com.openlib.market.domain.marketing;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BannerPromocionalTest {

    @Test
    void debeValidarPeriodoCorrectamente() {
        assertThrows(IllegalArgumentException.class, () -> 
            new PeriodoCampana(LocalDateTime.now().plusDays(1), LocalDateTime.now())
        );
    }

    @Test
    void bannerEstaVigenteSiEstaEnRangoYActivo() {
        PeriodoCampana periodo = new PeriodoCampana(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        BannerPromocional banner = new BannerPromocional("Promo", "img.png", "url.com", periodo);
        
        assertTrue(banner.estaVigente(LocalDateTime.now()));
    }

    @Test
    void bannerNoEstaVigenteSiFechaYaPaso() {
        PeriodoCampana periodo = new PeriodoCampana(LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1));
        BannerPromocional banner = new BannerPromocional("Promo", "img.png", "url.com", periodo);
        
        assertFalse(banner.estaVigente(LocalDateTime.now()));
    }

    @Test
    void bannerNoEstaVigenteSiEstaInactivo() {
        PeriodoCampana periodo = new PeriodoCampana(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        BannerPromocional banner = new BannerPromocional("Promo", "img.png", "url.com", periodo);
        banner.cambiarEstado(EstadoCampana.INACTIVA);
        
        assertFalse(banner.estaVigente(LocalDateTime.now()));
    }
}
