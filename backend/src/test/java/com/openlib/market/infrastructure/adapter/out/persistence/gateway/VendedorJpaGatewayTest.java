package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.VendedorEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.VendedorMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class VendedorJpaGatewayTest {

    @Autowired
    private VendedorJpaGateway vendedorJpaGateway;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Test
    public void testGuardarYBuscarVendedor() {
        Vendedor vendedor = new Vendedor("user123", new RazonSocial("Libros SA"), new IdentificacionTributaria("1234567890"));
        
        // Guardar
        vendedorJpaGateway.guardar(vendedor);

        // Buscar
        Optional<Vendedor> guardado = vendedorJpaGateway.obtenerPorId(vendedor.getId());

        assertTrue(guardado.isPresent());
        assertEquals("user123", guardado.get().getIdUsuario());
        assertEquals("Libros SA", guardado.get().getRazonSocial().getValor());
    }

    @Test
    public void testExistePorIdentificacionTributaria() {
        Vendedor vendedor = new Vendedor("user456", new RazonSocial("Tech Books"), new IdentificacionTributaria("0987654321"));
        vendedorJpaGateway.guardar(vendedor);

        assertTrue(vendedorJpaGateway.existePorIdentificacionTributaria("0987654321"));
        assertFalse(vendedorJpaGateway.existePorIdentificacionTributaria("1111111111"));
    }

    @Test
    public void testExcepcionIdentificacionDuplicada() {
        Vendedor vendedor1 = new Vendedor("user111", new RazonSocial("Editorial Uno"), new IdentificacionTributaria("9999999999"));
        Vendedor vendedor2 = new Vendedor("user222", new RazonSocial("Editorial Dos"), new IdentificacionTributaria("9999999999"));

        vendedorJpaGateway.guardar(vendedor1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vendedorJpaGateway.guardar(vendedor2);
        });

        assertEquals("La identificación tributaria ya se encuentra registrada", exception.getMessage());
    }
}
