package com.openlib.market.domain.dashboardGlobal;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SerieGraficaVentasTest {

    @Test
    void debeCalcularTotalAcumuladoCorrectamente() {
        SerieGraficaVentas serie = new SerieGraficaVentas(
                IntervaloTiempo.MENSUAL,
                List.of(
                        new PuntoDatos("Ene", 100.0),
                        new PuntoDatos("Feb", 250.0)
                )
        );

        assertEquals(350.0, serie.getTotalAcumuladoSerie(), 0.01);
    }
}
