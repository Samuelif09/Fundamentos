package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.finanzas.DatosFiscalesComprador;
import com.openlib.market.domain.finanzas.DatosFiscalesVendedor;
import com.openlib.market.domain.finanzas.DesgloseImpuestos;
import com.openlib.market.domain.finanzas.FacturaTributaria;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.FacturacionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({FacturacionJpaGateway.class, FacturacionMapper.class})
@Transactional
class FacturacionJpaGatewayIT {

    @Autowired
    private FacturacionJpaGateway gateway;

    @Test
    void debeGuardarYRecuperarFacturaFiscalCompleta() {
        DatosFiscalesVendedor vendedor = new DatosFiscalesVendedor("vend-1", "NIT-900", "Librería Central");
        DatosFiscalesComprador comprador = new DatosFiscalesComprador("comp-1", "Juan Perez", "juan@test.com");
        DesgloseImpuestos desglose = new DesgloseImpuestos(100000); // IVA = 19000, Total = 119000

        FacturaTributaria factura = new FacturaTributaria("pedido-123", vendedor, comprador, desglose);
        gateway.guardarFactura(factura);

        Optional<FacturaTributaria> recuperadaOpt = gateway.obtenerPorId(factura.getIdFactura());
        assertTrue(recuperadaOpt.isPresent());

        FacturaTributaria recuperada = recuperadaOpt.get();
        assertEquals("pedido-123", recuperada.getIdPedido());
        assertNotNull(recuperada.getFechaEmision());

        assertEquals("vend-1", recuperada.getVendedor().getIdVendedor());
        assertEquals("NIT-900", recuperada.getVendedor().getIdentificacionTributaria());

        assertEquals("comp-1", recuperada.getComprador().getIdUsuario());
        assertEquals("juan@test.com", recuperada.getComprador().getCorreo());

        assertEquals(100000, recuperada.getDesgloseImpuestos().getSubtotal());
        assertEquals(19000, recuperada.getDesgloseImpuestos().getIva());
        assertEquals(119000, recuperada.getDesgloseImpuestos().getTotal());
    }
}
