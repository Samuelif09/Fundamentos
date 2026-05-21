package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.configuracion.ConfiguracionMetodoPago;
import com.openlib.market.domain.configuracion.EstadoMetodoPago;
import com.openlib.market.domain.configuracion.NombreMetodo;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class MetodoPagoConfigJpaGatewayTest {

    @Autowired
    private MetodoPagoConfigJpaGateway gateway;

    @Test
    public void testHabilitarYDeshabilitarMetodoPago() {
        // Guardar dos métodos habilitados
        ConfiguracionMetodoPago paypal = new ConfiguracionMetodoPago(
                "mp-1", new NombreMetodo("PayPal"), EstadoMetodoPago.HABILITADO);
        ConfiguracionMetodoPago tarjeta = new ConfiguracionMetodoPago(
                "mp-2", new NombreMetodo("Tarjeta"), EstadoMetodoPago.HABILITADO);
        gateway.actualizar(paypal);
        gateway.actualizar(tarjeta);

        assertEquals(2, gateway.contarMetodosHabilitados());

        // Deshabilitar PayPal
        paypal.deshabilitar(2); // 2 activos → permitido
        gateway.actualizar(paypal);

        assertEquals(1, gateway.contarMetodosHabilitados());

        Optional<ConfiguracionMetodoPago> recuperado = gateway.obtenerPorId("mp-1");
        assertTrue(recuperado.isPresent());
        assertEquals(EstadoMetodoPago.DESHABILITADO, recuperado.get().getEstado());
    }

    @Test
    public void testListarTodosLosMetodos() {
        gateway.actualizar(new ConfiguracionMetodoPago(
                "mp-3", new NombreMetodo("Crypto"), EstadoMetodoPago.HABILITADO));
        gateway.actualizar(new ConfiguracionMetodoPago(
                "mp-4", new NombreMetodo("Transferencia"), EstadoMetodoPago.DESHABILITADO));

        List<ConfiguracionMetodoPago> todos = gateway.listarTodos();

        assertTrue(todos.size() >= 2);
    }
}
