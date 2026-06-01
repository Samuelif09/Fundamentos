package com.openlib.market.application.detalle;

import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.LibroNoEncontradoException;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;

import com.openlib.market.domain.shared.IEventPublisher;
import com.openlib.market.domain.historial.LibroVistoEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.openlib.market.domain.inventario.IPromocionGateway;
import com.openlib.market.domain.inventario.PromocionLibro;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.Usuario;

public class VerDetalleLibroInteractor implements IVerDetalleLibroUseCase {

    private final IDetalleGateway detalleGateway;
    private final IInventarioGateway inventarioGateway;
    private final IEventPublisher eventPublisher;
    private final IPromocionGateway promocionGateway;
    private final IUsuarioGateway usuarioGateway;

    public VerDetalleLibroInteractor(IDetalleGateway detalleGateway, IInventarioGateway inventarioGateway,
            IEventPublisher eventPublisher, IPromocionGateway promocionGateway) {
        this(detalleGateway, inventarioGateway, eventPublisher, promocionGateway, null);
    }

    public VerDetalleLibroInteractor(IDetalleGateway detalleGateway, IInventarioGateway inventarioGateway,
            IEventPublisher eventPublisher, IPromocionGateway promocionGateway, IUsuarioGateway usuarioGateway) {
        this.detalleGateway = detalleGateway;
        this.inventarioGateway = inventarioGateway;
        this.eventPublisher = eventPublisher;
        this.promocionGateway = promocionGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public LibroDetalleCompradorDto verDetalle(String isbn) {
        return verDetalle(isbn, null);
    }

    @Override
    public LibroDetalleCompradorDto verDetalle(String isbn, String idUsuario) {
        Isbn isbnDomain = new Isbn(isbn);

        // 1. Obtener datos del catálogo
        Optional<Libro> libroOpt = detalleGateway.buscarPorId(isbnDomain);
        if (libroOpt.isEmpty()) {
            throw new LibroNoEncontradoException(isbnDomain.getValor());
        }

        Libro libro = libroOpt.get();

        // 2. Obtener disponibilidad del inventario
        Optional<StockDisponible> stockOpt = inventarioGateway.obtenerStock(isbnDomain.getValor());

        // Si no existe registro en inventario, asumimos 0 stock (no disponible)
        boolean disponible = stockOpt.map(StockDisponible::isDisponible).orElse(false);

        // 3. Emitir evento asíncrono (Observer)
        if (idUsuario != null && eventPublisher != null) {
            eventPublisher.publicar(new LibroVistoEvent(idUsuario, libro.getIsbn().getValor()));
        }

        // 4. Buscar promociones activas
        double precioFinal = libro.getPrecio().getValor();
        if (promocionGateway != null) {
            List<PromocionLibro> promociones = promocionGateway.obtenerPorIsbn(isbnDomain.getValor());
            LocalDate hoy = LocalDate.now();
            for (PromocionLibro promo : promociones) {
                if (promo.getPeriodo().estaActivo(hoy)) {
                    precioFinal = precioFinal * (1 - (promo.getDescuento().getValor() / 100.0));
                    break; // Solo aplicamos una promo si está activa
                }
            }
        }

        // Obtener el nombre del vendedor como autor
        String autor = "Desconocido";
        if (usuarioGateway != null && libro.getIdVendedor() != null) {
            autor = usuarioGateway.buscarPorId(libro.getIdVendedor())
                    .map(Usuario::getNombre)
                    .orElse("Desconocido");
        }

        // Normalizar la url de la portada
        String urlPortada = libro.getUrlPortada();
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

        // 5. Consolidar la información para el comprador
        return new LibroDetalleCompradorDto(
                libro.getIsbn().getValor(),
                libro.getTitulo(),
                libro.getSinopsis(),
                precioFinal,
                disponible,
                autor,
                urlPortada);
    }
}
