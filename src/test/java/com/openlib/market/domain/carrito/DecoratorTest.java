package com.openlib.market.domain.carrito;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {

    @Test
    void debeCalcularSubtotalBaseSinDescuento() {
        ICalculadorSubtotal base = new CalculadorSubtotalBase();
        List<ItemCarrito> items = List.of(
                new ItemCarrito("123", new Cantidad(2), 10.0), // 20
                new ItemCarrito("456", new Cantidad(1), 5.0)   // 5
        );
        assertEquals(25.0, base.calcular(items));
    }

    @Test
    void debeAplicarDescuentoFijoSiSuperaLimite() {
        ICalculadorSubtotal base = new CalculadorSubtotalBase();
        // Descuento de 5.0 si la compra es >= 20.0
        ICalculadorSubtotal conDescuento = new DescuentoFijoDecorator(base, 5.0, 20.0);
        
        List<ItemCarrito> items = List.of(new ItemCarrito("123", new Cantidad(3), 10.0)); // 30.0
        
        // 30 - 5 = 25
        assertEquals(25.0, conDescuento.calcular(items));
    }

    @Test
    void noDebeAplicarDescuentoSiNoSuperaLimite() {
        ICalculadorSubtotal base = new CalculadorSubtotalBase();
        ICalculadorSubtotal conDescuento = new DescuentoFijoDecorator(base, 5.0, 50.0);
        
        List<ItemCarrito> items = List.of(new ItemCarrito("123", new Cantidad(3), 10.0)); // 30.0
        
        // No supera 50.0, queda en 30.0
        assertEquals(30.0, conDescuento.calcular(items));
    }

    @Test
    void carritoDebeUsarElCalculadorInyectado() {
        CarritoCompras carrito = new CarritoCompras(new IdUsuario("user-1"));
        carrito.agregarItem(new LibroSnapshot("123", 20.0), new Cantidad(2)); // Subtotal 40
        
        ICalculadorSubtotal base = new CalculadorSubtotalBase();
        ICalculadorSubtotal descuento = new DescuentoFijoDecorator(base, 10.0, 30.0);
        
        carrito.setCalculadorSubtotal(descuento);
        
        // 40 - 10 = 30
        assertEquals(30.0, carrito.getTotal());
    }
}
