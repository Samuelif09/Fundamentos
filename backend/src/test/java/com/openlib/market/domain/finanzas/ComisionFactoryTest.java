package com.openlib.market.domain.finanzas;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
class ComisionFactoryTest {
    private final ComisionFactory factory = new ComisionFactory();
    @Test
    void debeRetornarComisionFisicaParaLibro() {
        IComisionStrategy strategy = factory.obtenerEstrategia("LIBRO");
        BigDecimal comision = strategy.calcularComision(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("10.00"), comision);
    }
    @Test
    void debeRetornarComisionDigitalParaAudiolibro() {
        IComisionStrategy strategy = factory.obtenerEstrategia("AUDIOLIBRO");
        BigDecimal comision = strategy.calcularComision(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("20.00"), comision);
    }
}
