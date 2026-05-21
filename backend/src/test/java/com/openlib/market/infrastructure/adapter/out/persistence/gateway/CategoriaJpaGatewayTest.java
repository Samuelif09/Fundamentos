package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.categoria.CategoriaCatalogo;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class CategoriaJpaGatewayTest {

    @Autowired
    private CategoriaJpaGateway categoriaJpaGateway;

    @Test
    public void testGuardarYBuscarCategoria() {
        CategoriaCatalogo categoria = new CategoriaCatalogo(new com.openlib.market.domain.categoria.NombreCategoria("Ficcion"));
        
        categoriaJpaGateway.guardar(categoria);

        Optional<CategoriaCatalogo> guardada = categoriaJpaGateway.obtenerPorId(categoria.getId());

        assertTrue(guardada.isPresent());
        assertEquals("Ficcion", guardada.get().getNombre().getValor());
    }

    @Test
    public void testBuscarPorNombreNormalizado() {
        CategoriaCatalogo categoria = new CategoriaCatalogo(new com.openlib.market.domain.categoria.NombreCategoria("Ciencia Ficción"));
        categoriaJpaGateway.guardar(categoria);

        assertTrue(categoriaJpaGateway.existePorNombreNormalizado("Ciencia Ficción"));
        assertTrue(categoriaJpaGateway.existePorNombreNormalizado("ciencia ficción"));
        assertFalse(categoriaJpaGateway.existePorNombreNormalizado("no existe"));
    }
}
