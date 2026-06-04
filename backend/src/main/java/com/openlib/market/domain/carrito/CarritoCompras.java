package com.openlib.market.domain.carrito;

import com.openlib.market.domain.cupon.CuponDescuento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoCompras {
    private SesionId sesionId;
    private IdUsuario idUsuario;
    private final List<ItemCarrito> items;
    private ICalculadorSubtotal calculadorSubtotal;
    private CuponDescuento cuponAplicado;

    public CarritoCompras(SesionId sesionId) {
        this.sesionId = sesionId;
        this.items = new ArrayList<>();
        this.calculadorSubtotal = new CalculadorSubtotalBase();
    }

    public CarritoCompras(IdUsuario idUsuario) {
        this.idUsuario = idUsuario;
        this.items = new ArrayList<>();
        this.calculadorSubtotal = new CalculadorSubtotalBase();
    }

    public SesionId getSesionId() { return sesionId; }
    public IdUsuario getIdUsuario() { return idUsuario; }

    public void setCalculadorSubtotal(ICalculadorSubtotal calculadorSubtotal) {
        this.calculadorSubtotal = calculadorSubtotal;
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }

    public void agregarItem(LibroSnapshot libro, Cantidad cantidad) {
        Optional<ItemCarrito> itemExistente = items.stream()
                .filter(item -> item.getLibroIsbn().equals(libro.getIsbn()))
                .findFirst();

        if (itemExistente.isPresent()) {
            itemExistente.get().agregarCantidad(cantidad);
        } else {
            items.add(new ItemCarrito(libro.getIsbn(), cantidad, libro.getPrecio()));
        }
    }

    public void removerItem(String isbn) {
        items.removeIf(item -> item.getLibroIsbn().equals(isbn));
    }

    public void actualizarCantidad(String isbn, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            removerItem(isbn);
            return;
        }
        for (ItemCarrito item : items) {
            if (item.getLibroIsbn().equals(isbn)) {
                item.setCantidad(new Cantidad(nuevaCantidad));
                break;
            }
        }
    }

    public void aplicarDescuento(CuponDescuento cupon) {
        this.cuponAplicado = cupon;
    }

    public double getTotal() {
        double subtotal = calculadorSubtotal.calcular(items);
        if (cuponAplicado != null) {
            return cuponAplicado.aplicarDescuento(subtotal);
        }
        return subtotal;
    }

    public void vaciar() {
        this.items.clear();
        this.cuponAplicado = null;
    }
}
