package com.openlib.market.domain.dashboard;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionDashboardTest {

    @Test
    void debeRequerirIdAdminValido() {
        assertThrows(IllegalArgumentException.class, () -> new ConfiguracionDashboard(null));
        assertThrows(IllegalArgumentException.class, () -> new ConfiguracionDashboard(""));
    }

    @Test
    void debeCrearConfiguracionVaciaPorDefecto() {
        ConfiguracionDashboard config = new ConfiguracionDashboard("admin-1");
        assertTrue(config.getWidgets().isEmpty());
    }

    @Test
    void debeActualizarWidgetsCorrectamente() {
        ConfiguracionDashboard config = new ConfiguracionDashboard("admin-1");
        
        Widget widget1 = new Widget(TipoWidget.GRAFICO_VENTAS, new Posicion(0, 0), new Tamano(2, 2));
        Widget widget2 = new Widget(TipoWidget.RESUMEN_USUARIOS, new Posicion(2, 0), new Tamano(1, 1));
        
        config.actualizarWidgets(List.of(widget1, widget2));
        
        assertEquals(2, config.getWidgets().size());
        assertEquals(TipoWidget.GRAFICO_VENTAS, config.getWidgets().get(0).getTipo());
    }

    @Test
    void noDebePermitirWidgetsConPosicionesNegativas() {
        assertThrows(IllegalArgumentException.class, () -> new Posicion(-1, 5));
    }
}
