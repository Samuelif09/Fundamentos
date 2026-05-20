package com.openlib.market.domain.carrito;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarritoComprasTest {

    @Test
    void debeCalcularTotalCorrectamenteEIncrementarCantidadSiItemExiste() {
        CarritoCompras carrito = new CarritoCompras(new SesionId("sesion-123"));
        LibroSnapshot libro1 = new LibroSnapshot("isbn-1", 10.0);
        LibroSnapshot libro2 = new LibroSnapshot("isbn-2", 20.0);

        carrito.agregarItem(libro1, new Cantidad(1));
        carrito.agregarItem(libro2, new Cantidad(2)); // subtotal 40

        assertEquals(2, carrito.getItems().size());
        assertEquals(50.0, carrito.getTotal());

        // Agregar el mismo libro1
        carrito.agregarItem(libro1, new Cantidad(2)); // Total cantidad = 3 -> subtotal 30
        
        assertEquals(2, carrito.getItems().size(), "No debe duplicar el item");
        assertEquals(70.0, carrito.getTotal(), "Total debe ser 30 + 40 = 70");
        
        ItemCarrito item1 = carrito.getItems().stream().filter(i -> i.getLibroIsbn().equals("isbn-1")).findFirst().get();
        assertEquals(3, item1.getCantidad().getValor());
    }
}
