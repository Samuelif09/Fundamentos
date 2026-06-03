package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.IdUsuario;
import com.openlib.market.domain.carrito.ItemCarrito;
import com.openlib.market.domain.checkout.CalculadorPrecio;
import com.openlib.market.domain.checkout.ImpuestoDecorator;
import com.openlib.market.domain.checkout.PrecioBase;
import com.openlib.market.domain.detalle.ContenidoDigital;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VerCarritoInteractor implements IVerCarritoUseCase {

    private final ICarritoGateway carritoGateway;
    private final IContenidoDigitalGateway contenidoGateway;

    public VerCarritoInteractor(ICarritoGateway carritoGateway, IContenidoDigitalGateway contenidoGateway) {
        this.carritoGateway = carritoGateway;
        this.contenidoGateway = contenidoGateway;
    }

    @Override
    public CarritoResponseDto verCarritoUsuario(String userId) {
        Optional<CarritoCompras> optCarrito = carritoGateway.obtenerPorUsuario(new IdUsuario(userId));

        if (optCarrito.isEmpty()) {
            return new CarritoResponseDto(userId, new ArrayList<>(), 0.0);
        }

        CarritoCompras carrito = optCarrito.get();
        List<CarritoItemDto> itemsDto = new ArrayList<>();

        for (ItemCarrito item : carrito.getItems()) {
            String isbn = item.getLibroIsbn();
            String titulo = contenidoGateway.obtenerContenidoPorId(isbn)
                    .map(ContenidoDigital::getTitulo)
                    .orElse("Producto Desconocido");

            itemsDto.add(new CarritoItemDto(
                    isbn,
                    titulo,
                    item.getCantidad().getValor(),
                    item.getPrecioUnitario()
            ));
        }

        CalculadorPrecio calculadorBase = new PrecioBase(carrito.getTotal());
        CalculadorPrecio calculadorConImpuesto = new ImpuestoDecorator(calculadorBase, 0.19);
        double totalCalculado = calculadorConImpuesto.calcularTotal();

        return new CarritoResponseDto(userId, itemsDto, totalCalculado);
    }
}
