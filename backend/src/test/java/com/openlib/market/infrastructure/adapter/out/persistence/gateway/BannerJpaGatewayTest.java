package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.marketing.*;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class BannerJpaGatewayTest {

    @Autowired
    private BannerJpaGateway gateway;

    @Test
    public void testVigenciaTemporal() {
        LocalDateTime ahora = LocalDateTime.now();

        // Banner vigente: período que incluye ahora
        BannerPromocional vigente = new BannerPromocional("Verano 2025", "/img/verano.jpg", "/verano",
                new PeriodoCampana(ahora.minusDays(5), ahora.plusDays(5)));
        gateway.guardar(vigente);

        // Banner expirado: período anterior a ahora
        BannerPromocional expirado = new BannerPromocional("Navidad 2024", "/img/nav.jpg", "/nav",
                new PeriodoCampana(ahora.minusDays(60), ahora.minusDays(30)));
        gateway.guardar(expirado);

        // Banner futuro: período aún no iniciado
        BannerPromocional futuro = new BannerPromocional("Otoño 2025", "/img/otono.jpg", "/otono",
                new PeriodoCampana(ahora.plusDays(10), ahora.plusDays(20)));
        gateway.guardar(futuro);

        List<BannerPromocional> vigentes = gateway.listarVigentes(ahora);
        assertEquals(1, vigentes.size(), "Solo el banner de Verano debe estar vigente");
        assertEquals("Verano 2025", vigentes.get(0).getTitulo());
        assertTrue(vigentes.get(0).estaVigente(ahora));
    }

    @Test
    public void testCambiarEstadoAInactiva() {
        LocalDateTime ahora = LocalDateTime.now();
        BannerPromocional banner = new BannerPromocional("Promo Flash", "/img/flash.jpg", "/flash",
                new PeriodoCampana(ahora.minusDays(1), ahora.plusDays(1)));
        gateway.guardar(banner);

        // Verificar que está vigente
        assertEquals(1, gateway.listarVigentes(ahora).size());

        // Inactivar manualmente
        banner.cambiarEstado(EstadoCampana.INACTIVA);
        gateway.actualizar(banner);

        // Ya no debe aparecer en los vigentes
        assertEquals(0, gateway.listarVigentes(ahora).size());
        assertFalse(gateway.obtenerPorId(banner.getId()).estaVigente(ahora));
    }
}
