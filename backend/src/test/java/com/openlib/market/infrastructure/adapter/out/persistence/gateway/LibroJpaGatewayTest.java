package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class LibroJpaGatewayTest {

    @Autowired
    private LibroJpaGateway libroJpaGateway;

    @Test
    public void testGuardarYBuscarLibro() {
        Libro libro = new Libro(
                new Isbn("978-3-16-148410-0"),
                "El Quijote",
                "En un lugar de la Mancha...",
                new Precio(19.99),
                "http://portada.com",
                "Clasicos",
                "user-1"
        );
        
        libroJpaGateway.actualizar(libro);

        Optional<Libro> guardado = libroJpaGateway.buscarPorIsbn(new Isbn("978-3-16-148410-0"));
        
        assertTrue(guardado.isPresent());
        assertEquals("El Quijote", guardado.get().getTitulo());
        assertEquals(19.99, guardado.get().getPrecio().getValor());
        assertEquals("Clasicos", guardado.get().getCategoria());
        assertEquals("user-1", guardado.get().getIdVendedor());
    }
}
