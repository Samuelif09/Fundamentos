package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.categoria.CategoriaCatalogo;
import com.openlib.market.domain.categoria.NombreCategoria;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.CategoriaMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CategoriaJpaGateway.class, CategoriaMapper.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CategoriaJpaGatewayIT {

    @Autowired
    private CategoriaJpaGateway gateway;

    @Test
    void debeGuardarYRecuperarCategoria() {
        CategoriaCatalogo categoria = new CategoriaCatalogo(new NombreCategoria("Ciencia Ficción"));
        gateway.guardar(categoria);

        Optional<CategoriaCatalogo> recuperada = gateway.obtenerPorId(categoria.getId());
        assertTrue(recuperada.isPresent());
        assertEquals("Ciencia ficción", recuperada.get().getNombre().getValor());
    }

    @Test
    void debeVerificarSiExistePorNombre() {
        CategoriaCatalogo categoria = new CategoriaCatalogo(new NombreCategoria("Fantasía"));
        gateway.guardar(categoria);

        assertTrue(gateway.existePorNombreNormalizado("fantasía"));
        assertFalse(gateway.existePorNombreNormalizado("ciencia"));
    }
}
