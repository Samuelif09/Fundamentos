package com.openlib.market.domain.cupon;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CuponDescuentoTest {

    @Test
    void debeLanzarExcepcionSiCuponEstaExpirado() {
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("DESC10"),
                new DescuentoPorcentaje(10),
                LocalDate.now().minusDays(1) // Expirado ayer
        );

        assertThrows(CuponExpiradoException.class, () -> cupon.validar(LocalDate.now()));
    }

    @Test
    void debeEstarVigenteSiFechaNoHaPasado() {
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("PROMO20"),
                new DescuentoPorcentaje(20),
                LocalDate.now().plusDays(30)
        );

        assertDoesNotThrow(() -> cupon.validar(LocalDate.now()));
    }

    @Test
    void debeAplicarDescuentoPorcentajeCorrectamente() {
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("DESC10"),
                new DescuentoPorcentaje(10),
                LocalDate.now().plusDays(10)
        );

        // 100 - 10% = 90
        assertEquals(90.0, cupon.aplicarDescuento(100.0), 0.001);
    }

    @Test
    void debeAplicarDescuentoMontoFijoCorrectamente() {
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("MENOS5"),
                new DescuentoMontoFijo(5.0),
                LocalDate.now().plusDays(10)
        );

        // 30 - 5 = 25
        assertEquals(25.0, cupon.aplicarDescuento(30.0), 0.001);
    }

    @Test
    void elTotalNuncaDebeSerNegativoConMontoFijo() {
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("GRATIS"),
                new DescuentoMontoFijo(999.0),
                LocalDate.now().plusDays(10)
        );

        assertEquals(0.0, cupon.aplicarDescuento(10.0), 0.001);
    }
}
