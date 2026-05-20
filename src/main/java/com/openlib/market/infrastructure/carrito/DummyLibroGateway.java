package com.openlib.market.infrastructure.carrito;

import com.openlib.market.domain.carrito.ILibroGateway;
import com.openlib.market.domain.carrito.LibroSnapshot;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DummyLibroGateway implements ILibroGateway {

    @Override
    public Optional<LibroSnapshot> obtenerPorIsbn(String isbn) {
        // En un entorno real esto consultaría la base de datos o el módulo de Catálogo/Detalle
        // Simulamos que el libro existe para "Entrega 1"
        if ("isbn-inv".equals(isbn)) {
            return Optional.empty();
        }
        return Optional.of(new LibroSnapshot(isbn, 29.99));
    }
}
