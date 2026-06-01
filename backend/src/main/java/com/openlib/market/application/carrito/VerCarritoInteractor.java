package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.*;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VerCarritoInteractor implements IVerCarritoUseCase {

    private final ICarritoGateway carritoGateway;
    private final IDetalleGateway detalleGateway;
    private final IUsuarioGateway usuarioGateway;

    public VerCarritoInteractor(ICarritoGateway carritoGateway, IDetalleGateway detalleGateway, IUsuarioGateway usuarioGateway) {
        this.carritoGateway = carritoGateway;
        this.detalleGateway = detalleGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public CartDto verCarrito(String userId) {
        IdUsuario idUsuario = new IdUsuario(userId);
        CarritoCompras carrito = carritoGateway.obtenerPorUsuario(idUsuario)
                .orElse(new CarritoCompras(idUsuario));

        // Aplicar la lógica de descuento fijo (patrón Decorator)
        ICalculadorSubtotal base = new CalculadorSubtotalBase();
        ICalculadorSubtotal conDescuento = new DescuentoFijoDecorator(base, 10.0, 50.0);
        carrito.setCalculadorSubtotal(conDescuento);

        List<CartItemDto> itemDtos = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            String isbnStr = item.getLibroIsbn();
            Optional<Libro> libroOpt = detalleGateway.buscarPorId(new Isbn(isbnStr));
            
            String titulo = "Producto Desconocido";
            String autor = "Desconocido";
            double precio = item.getPrecioUnitario();
            String urlPortada = "";
            String sinopsis = "";

            if (libroOpt.isPresent()) {
                Libro libro = libroOpt.get();
                titulo = libro.getTitulo();
                sinopsis = libro.getSinopsis();
                precio = libro.getPrecio().getValor();

                // Recuperar autor/vendedor
                if (libro.getIdVendedor() != null) {
                    autor = usuarioGateway.buscarPorId(libro.getIdVendedor())
                            .map(Usuario::getNombre)
                            .orElse("Desconocido");
                }

                // Normalizar portada
                urlPortada = libro.getUrlPortada();
                if (urlPortada != null && !urlPortada.isBlank()) {
                    urlPortada = urlPortada.replace("\\", "/");
                    if (!urlPortada.startsWith("http://") && !urlPortada.startsWith("https://")) {
                        if (urlPortada.startsWith("/")) {
                            urlPortada = "http://localhost:8080" + urlPortada;
                        } else {
                            urlPortada = "http://localhost:8080/" + urlPortada;
                        }
                    }
                }
            }

            CartBookDto bookDto = new CartBookDto(isbnStr, titulo, autor, precio, urlPortada, sinopsis);
            itemDtos.add(new CartItemDto(isbnStr, bookDto, item.getCantidad().getValor()));
        }

        double subtotal = carrito.getTotal();
        double taxes = subtotal * 0.19; // 19% impuesto
        double total = subtotal + taxes;

        return new CartDto(itemDtos, subtotal, taxes, total);
    }
}
