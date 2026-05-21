package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.VendedorEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.VendedorMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({VendedorJpaGateway.class, VendedorMapper.class})
@Transactional
class VendedorJpaGatewayIT {

    @Autowired
    private VendedorJpaGateway gateway;

    @Autowired
    private VendedorRepository repository;

    @Test
    void debeGuardarYRecuperarVendedorPorId() {
        Vendedor nuevoVendedor = new Vendedor("usr-123", new RazonSocial("Mi Empresa SAS"), new IdentificacionTributaria("123456789"));
        
        gateway.guardar(nuevoVendedor);

        Optional<Vendedor> recuperado = gateway.obtenerPorId(nuevoVendedor.getId());
        assertTrue(recuperado.isPresent());
        assertEquals("Mi Empresa SAS", recuperado.get().getRazonSocial().getValor());
        assertEquals("123456789", recuperado.get().getIdentificacionTributaria().getValor());

        // Verificando entidad
        Optional<VendedorEntity> entity = repository.findById(nuevoVendedor.getId());
        assertTrue(entity.isPresent());
        assertEquals("usr-123", entity.get().getIdUsuario());
    }

    @Test
    void debeVerificarExistenciaPorIdentificacionTributaria() {
        Vendedor nuevoVendedor = new Vendedor("usr-456", new RazonSocial("Empresa B"), new IdentificacionTributaria("987654321"));
        gateway.guardar(nuevoVendedor);

        assertTrue(gateway.existePorIdentificacionTributaria("987654321"));
        assertFalse(gateway.existePorIdentificacionTributaria("000000000"));
    }

    @Test
    void debeLanzarExcepcionAlGuardarIdTributariaDuplicada() {
        Vendedor vendedor1 = new Vendedor("usr-1", new RazonSocial("Empresa A"), new IdentificacionTributaria("111111111"));
        gateway.guardar(vendedor1);

        Vendedor vendedor2 = new Vendedor("usr-2", new RazonSocial("Empresa B"), new IdentificacionTributaria("111111111"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gateway.guardar(vendedor2);
            repository.flush();
        });
        
        assertEquals("La identificación tributaria ya se encuentra registrada", exception.getMessage());
    }
}
