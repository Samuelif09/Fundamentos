package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.catalogo.*;
import com.openlib.market.domain.filtroprecio.RangoPrecio;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CatalogoJpaGateway.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CatalogoJpaGatewayIT {

    @Autowired
    private CatalogoJpaGateway gateway;

    @Autowired
    private ContenidoDigitalRepository repository;

    @BeforeEach
    void setUp() {
        repository.save(new LibroEntity("1", "Java Avanzado", "...", 50000, "url1", "Programacion", "vend1", "ACTIVO", "urlv", 10));
        repository.save(new LibroEntity("2", "Python Básico", "...", 30000, "url2", "Programacion", "vend1", "ACTIVO", "urlv", 20));
        repository.save(new LibroEntity("3", "Historia del Arte", "...", 15000, "url3", "Historia", "vend1", "ACTIVO", "urlv", 5));
        repository.flush();
    }

    @Test
    void debeListarPaginado() {
        Paginacion paginacion = new Paginacion(0, 2);
        PaginaDominio<LibroCatalogo> pagina = gateway.listarPaginado(paginacion);

        assertEquals(2, pagina.getContenido().size());
        assertEquals(3, pagina.getTotalElementos());
        assertEquals(2, pagina.getTotalPaginas());
        assertTrue(pagina.hasNext());
    }

    @Test
    void debeBuscarPorFiltrosCombinados() {
        CriterioBusqueda criterio = new CriterioBusqueda("Java", "", "Programacion", new RangoPrecio(10000.0, 60000.0));
        List<LibroCatalogo> resultados = gateway.buscarPorFiltros(criterio);

        assertEquals(1, resultados.size());
        assertEquals("Java Avanzado", resultados.get(0).titulo());
    }
}
