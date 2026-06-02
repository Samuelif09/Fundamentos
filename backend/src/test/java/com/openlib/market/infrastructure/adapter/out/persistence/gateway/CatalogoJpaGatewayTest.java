package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.catalogo.CriterioBusqueda;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.domain.catalogo.PaginaDominio;
import com.openlib.market.domain.catalogo.Paginacion;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.LibroRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class CatalogoJpaGatewayTest {

    @Autowired
    private CatalogoJpaGateway catalogoJpaGateway;

    @Autowired
    private LibroJpaGateway libroJpaGateway;

    @Autowired
    private LibroRepository libroRepository;

    @BeforeEach
    public void setUp() {
        libroRepository.deleteAll();
        libroJpaGateway.actualizar(new Libro(new Isbn("111"), "Spring Boot in Action", "...", new Precio(30.0), "url", "Programacion", "u1"));
        libroJpaGateway.actualizar(new Libro(new Isbn("222"), "Clean Architecture", "...", new Precio(40.0), "url", "Arquitectura", "u1"));
        libroJpaGateway.actualizar(new Libro(new Isbn("333"), "Clean Code", "...", new Precio(35.0), "url", "Programacion", "u2"));
        libroJpaGateway.actualizar(new Libro(new Isbn("444"), "The Hobbit", "...", new Precio(15.0), "url", "Fantasia", "u2"));
        libroJpaGateway.actualizar(new Libro(new Isbn("555"), "Spring Microservices", "...", new Precio(45.0), "url", "Programacion", "u3"));
    }

    @Test
    public void testPaginacion() {
        Paginacion paginacion = new Paginacion(0, 2);
        PaginaDominio<LibroCatalogo> pagina = catalogoJpaGateway.listarPaginado(paginacion);

        assertNotNull(pagina);
        assertEquals(2, pagina.getContenido().size());
        assertEquals(0, pagina.getPaginaActual());
        assertEquals(2, pagina.getTamanoPagina());
        assertEquals(5, pagina.getTotalElementos());
    }

    @Test
    public void testBusquedaConFiltrosCombinados() {
        // Buscar por titulo "Clean"
        CriterioBusqueda criterioTitulo = new CriterioBusqueda("Clean", null, null, null);
        List<LibroCatalogo> resultadosTitulo = catalogoJpaGateway.buscarPorFiltros(criterioTitulo);
        assertEquals(2, resultadosTitulo.size());
        
        // Buscar por categoria "Programacion" y rango de precio 25-35
        CriterioBusqueda criterioCombinado = new CriterioBusqueda(null, null, "Programacion", new com.openlib.market.domain.filtroprecio.RangoPrecio(25.0, 35.0));
        List<LibroCatalogo> resultadosCombinados = catalogoJpaGateway.buscarPorFiltros(criterioCombinado);
        
        assertEquals(2, resultadosCombinados.size());
        assertTrue(resultadosCombinados.stream().anyMatch(l -> l.titulo().equals("Spring Boot in Action")));
        assertTrue(resultadosCombinados.stream().anyMatch(l -> l.titulo().equals("Clean Code")));
    }
}
