package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.inventario.StockDisponible;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({InventarioJpaGateway.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class InventarioJpaGatewayIT {

    @Autowired
    private InventarioJpaGateway gateway;

    @Autowired
    private ContenidoDigitalRepository repository;

    @Test
    void debeObtenerStockDisponiblePorIsbn() {
        LibroEntity entity = new LibroEntity("978-0-13-468599-1", "Effective Java", "...", 120000, "", "Programming", "v1", "ACTIVO", "", 15);
        repository.save(entity);
        repository.flush();

        Optional<StockDisponible> stock = gateway.obtenerStock("978-0-13-468599-1");
        assertTrue(stock.isPresent());
        assertEquals(15, stock.get().getCantidad());
    }

    @Test
    void noDebeEncontrarStockSiNoExisteLibro() {
        Optional<StockDisponible> stock = gateway.obtenerStock("INEXISTENTE");
        assertFalse(stock.isPresent());
    }
}
