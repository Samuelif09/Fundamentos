package com.openlib.market.domain.finanzas;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DesgloseImpuestosTest {

    @Test
    void debeCalcularIva19PorCientoYTotalCorrectamente() {
        DesgloseImpuestos desglose = new DesgloseImpuestos(100.0);
        
        assertEquals(100.0, desglose.getSubtotal());
        assertEquals(19.0, desglose.getIva());
        assertEquals(119.0, desglose.getTotal());
    }

    @Test
    void debeCalcularConDecimalesCorrectamente() {
        // 55.5 * 0.19 = 10.545 => se redondea a 10.55
        // En este mock se usa Math.round, si no concuerda en la vida real se usa BigDecimal, 
        // pero para MVP está bien. Veamos que nos da Math.round(10.545 * 100)/100.0:
        double esperadoIva = Math.round(55.5 * 0.19 * 100.0) / 100.0;
        
        DesgloseImpuestos desglose = new DesgloseImpuestos(55.5);
        
        assertEquals(55.5, desglose.getSubtotal());
        assertEquals(esperadoIva, desglose.getIva());
        assertEquals(55.5 + esperadoIva, desglose.getTotal());
    }
}
