package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.*;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.domain.detalle.ContenidoDigital;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.shared.AccionNoPermitidaException;

public class AgregarCarritoInteractor implements IAgregarCarritoUseCase {

    private final ICarritoGateway carritoGateway;
    private final ILibroGateway libroGateway;
    private final IInventarioGateway inventarioGateway;
    private final IContenidoDigitalGateway contenidoGateway;

    public AgregarCarritoInteractor(ICarritoGateway carritoGateway, ILibroGateway libroGateway, IInventarioGateway inventarioGateway, IContenidoDigitalGateway contenidoGateway) {
        this.carritoGateway = carritoGateway;
        this.libroGateway = libroGateway;
        this.inventarioGateway = inventarioGateway;
        this.contenidoGateway = contenidoGateway;
    }

    @Override
    public void agregarAlCarrito(AgregarItemRequestDto request) {
        Cantidad cantidad = new Cantidad(request.getCantidad());

        ContenidoDigital contenido = contenidoGateway.obtenerContenidoPorId(request.getLibroIsbn())
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe en el catálogo"));

        if (contenido.getEstado() != EstadoLibro.PUBLICADO) {
            throw new AccionNoPermitidaException("Solo se pueden agregar productos publicados al carrito");
        }

        if (contenido.requiereControlDeInventario()) {
            // Validar Stock
            StockDisponible stock = inventarioGateway.obtenerStock(request.getLibroIsbn())
                    .orElse(new StockDisponible(0));

            if (stock.getCantidad() < cantidad.getValor()) {
                throw new StockInsuficienteException(request.getLibroIsbn(), cantidad.getValor(), stock.getCantidad());
            }
        }

        LibroSnapshot libro = libroGateway.obtenerPorIsbn(request.getLibroIsbn())
                .orElseThrow(() -> new IllegalArgumentException("El libro no existe"));

        CarritoCompras carrito;
        if (request.getIdUsuario() != null && !request.getIdUsuario().isEmpty()) {
            IdUsuario idUsuario = new IdUsuario(request.getIdUsuario());
            carrito = carritoGateway.obtenerPorUsuario(idUsuario).orElse(new CarritoCompras(idUsuario));
            
            // Ejemplo de uso de patrón Decorator para compradores registrados (ej. Descuento Fijo si supera 50)
            ICalculadorSubtotal base = new CalculadorSubtotalBase();
            ICalculadorSubtotal conDescuento = new DescuentoFijoDecorator(base, 10.0, 50.0);
            carrito.setCalculadorSubtotal(conDescuento);
        } else {
            SesionId sesionId = new SesionId(request.getSesionId());
            carrito = carritoGateway.obtenerPorSesionId(sesionId).orElse(new CarritoCompras(sesionId));
        }

        carrito.agregarItem(libro, cantidad);
        carritoGateway.guardar(carrito);
    }
}
