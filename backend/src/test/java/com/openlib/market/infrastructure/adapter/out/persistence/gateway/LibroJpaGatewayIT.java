package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.LibroMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({LibroJpaGateway.class, LibroMapper.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(properties = {
    "spring.jpa.properties.hibernate.discriminator.ignore_explicit_for_joined=true",
    "spring.jpa.properties.hibernate.mapping.discriminator.check=false"
})
class LibroJpaGatewayIT {

    @Autowired
    private LibroJpaGateway gateway;

    @Autowired
    private ContenidoDigitalRepository repository;

    @Test
    void debeGuardarYRecuperarLibroPorIsbn() {
        Libro libro = new Libro(new Isbn("978-3-16-148410-0"), "Cien Años de Soledad", "Mágico", new Precio(50000), "url", "Ficción", "vendedor-1");
        
        gateway.actualizar(libro); // El gateway usa actualizar() que hace save()

        Optional<Libro> recuperado = gateway.buscarPorIsbn(new Isbn("978-3-16-148410-0"));
        assertTrue(recuperado.isPresent());
        assertEquals("Cien Años de Soledad", recuperado.get().getTitulo());
        assertEquals(50000, recuperado.get().getPrecio().getValor());

        // Verificando entidad subyacente
        Optional<ContenidoDigitalEntity> entity = repository.findById("978-3-16-148410-0");
        assertTrue(entity.isPresent());
        assertEquals("Ficción", entity.get().getCategoria());
    }

    @Test
    void debeActualizarEstadoLibro() {
        Libro libro = new Libro(new Isbn("123-4-56-789012-3"), "Libro Prueba", "Sinopsis", new Precio(10000), "url");
        gateway.actualizar(libro);

        Libro libroPausado = libro.pausar();
        gateway.actualizar(libroPausado);

        Optional<Libro> recuperado = gateway.buscarPorIsbn(new Isbn("123-4-56-789012-3"));
        assertTrue(recuperado.isPresent());
        assertEquals(EstadoLibro.PAUSADO, recuperado.get().getEstado());
    }

    @Test
    void obtenerPorIsbnDesdeCarrito() {
        Libro libro = new Libro(new Isbn("999-9-99-999999-9"), "El Hobbit", "Aventura", new Precio(35000), "url-hobbit", "Fantasía", "vendedor-1");
        gateway.actualizar(libro);

        var snapshot = gateway.obtenerPorIsbn("999-9-99-999999-9");
        assertTrue(snapshot.isPresent());
        assertEquals("999-9-99-999999-9", snapshot.get().getIsbn());
        assertEquals(35000, snapshot.get().getPrecio());
    }
}
