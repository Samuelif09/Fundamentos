package com.openlib.market.domain.inventario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StockDisponibleTest {

    @Test
    void debeLanzarExcepcionSiStockEsNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new StockDisponible(-1));
    }

    @Test
    void debeRetornarDisponibleSiStockMayorACero() {
        StockDisponible stock = new StockDisponible(5);
        assertTrue(stock.isDisponible());
    }

    @Test
    void debeRetornarNoDisponibleSiStockEsCero() {
        StockDisponible stock = new StockDisponible(0);
        assertFalse(stock.isDisponible());
    }
}
