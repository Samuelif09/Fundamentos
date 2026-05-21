package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.dashboard.ConfiguracionDashboard;
import com.openlib.market.domain.dashboard.Posicion;
import com.openlib.market.domain.dashboard.Tamano;
import com.openlib.market.domain.dashboard.TipoWidget;
import com.openlib.market.domain.dashboard.Widget;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.ConfiguracionDashboardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ConfiguracionAdminJpaGateway.class, ConfiguracionDashboardMapper.class})
@Transactional
class ConfiguracionAdminJpaGatewayIT {

    @Autowired
    private ConfiguracionAdminJpaGateway gateway;

    @Test
    void debeGuardarYRecuperarConfiguracionDashboard() {
        ConfiguracionDashboard config = new ConfiguracionDashboard("admin-77");
        config.actualizarWidgets(List.of(
                new Widget(TipoWidget.GRAFICO_VENTAS, new Posicion(0, 0), new Tamano(2, 2)),
                new Widget(TipoWidget.ALERTAS_RECIENTES, new Posicion(2, 0), new Tamano(1, 2))
        ));

        gateway.guardar(config);

        Optional<ConfiguracionDashboard> recuperada = gateway.buscarPorAdminId("admin-77");
        assertTrue(recuperada.isPresent());
        assertEquals("admin-77", recuperada.get().getIdAdmin());
        assertEquals(2, recuperada.get().getWidgets().size());

        Widget w1 = recuperada.get().getWidgets().get(0);
        assertEquals(TipoWidget.GRAFICO_VENTAS, w1.getTipo());
        assertEquals(0, w1.getPosicion().x());
        assertEquals(2, w1.getTamano().ancho());
    }
}
