package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.configuracion.ReglaComision;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class ConfiguracionComisionJpaGatewayTest {

    @Autowired
    private ConfiguracionComisionJpaGateway gateway;

    @Test
    public void testGuardarYRecuperarReglaEspecifica() {
        ReglaComision regla = new ReglaComision("ficcion", 15.0);
        gateway.guardarRegla(regla);

        ReglaComision recuperada = gateway.obtenerRegla("FICCION");
        assertEquals(15.0, recuperada.getPorcentajeComision(), 0.001);
    }

    @Test
    public void testFallbackAGlobalCuandoNoExisteCategoria() {
        ReglaComision global = new ReglaComision("GLOBAL", 10.0);
        gateway.guardarRegla(global);

        // Pedir regla de categoria que no existe → debe devolver la GLOBAL
        ReglaComision resultado = gateway.obtenerRegla("CATEGORIA_INEXISTENTE");
        assertEquals(10.0, resultado.getPorcentajeComision(), 0.001);
        assertEquals("GLOBAL", resultado.getIdCategoria());
    }

    @Test
    public void testActualizarComisionSinDuplicados() {
        ReglaComision regla = new ReglaComision("tecnologia", 12.0);
        gateway.guardarRegla(regla);

        long countAntes = gateway.listarTodas().size();

        // Guardar de nuevo con el mismo idCategoria → upsert, no duplicado
        gateway.guardarRegla(new ReglaComision("TECNOLOGIA", 20.0));

        assertEquals(countAntes, gateway.listarTodas().size(), "No debe crear duplicados");
        assertEquals(20.0, gateway.obtenerRegla("TECNOLOGIA").getPorcentajeComision(), 0.001);
    }
}
