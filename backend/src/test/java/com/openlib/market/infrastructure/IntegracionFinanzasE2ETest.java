package com.openlib.market.infrastructure;

import com.openlib.market.application.checkout.ProcesarCheckoutInteractor;
import com.openlib.market.application.finanzas.GenerarReporteIngresosInteractor;
import com.openlib.market.application.finanzas.IngresosVendedorResponseDto;
import com.openlib.market.application.finanzas.GenerarRentabilidadPlataformaInteractor;
import com.openlib.market.application.finanzas.RentabilidadPlataformaResponseDto;
import com.openlib.market.domain.pago.Pedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class IntegracionFinanzasE2ETest {

    @Autowired
    private GenerarReporteIngresosInteractor reporteIngresosInteractor;

    // Aquí iría el Autowired del checkout y repositorios necesarios para crear el contexto
    // de prueba, pero como prueba inicial verificaremos que el contexto levante y los 
    // beans de Epic 3 y Epic 4 estén correctamente inyectados.

    @Autowired
    private GenerarRentabilidadPlataformaInteractor rentabilidadPlataformaInteractor;

    @Test
    void verificarGeneracionReporteContexto() {
        assertNotNull(reporteIngresosInteractor);
        
        // Ejecución en frío para asegurar que la ruta y query HQL de IVentasReadGateway no arroja error de sintaxis
        IngresosVendedorResponseDto reporte = reporteIngresosInteractor.ejecutar(
                "vendedor-test-123", 
                LocalDate.now().minusDays(1), 
                LocalDate.now().plusDays(1)
        );
        
        assertNotNull(reporte);
        assertNotNull(reporte.getIngresoNetoVendedor());
        assertNotNull(reporte.getTotalComisionesPlataforma());
        assertNotNull(reporte.getTotalVentasBrutas());
    }

    @Test
    void verificarGeneracionRentabilidadPlataformaContexto() {
        assertNotNull(rentabilidadPlataformaInteractor);

        // Ejecución en frío para asegurar query de rentabilidad HQL global
        RentabilidadPlataformaResponseDto rentabilidad = rentabilidadPlataformaInteractor.ejecutar(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        assertNotNull(rentabilidad);
        assertNotNull(rentabilidad.getTotalVentasBrutas());
        assertNotNull(rentabilidad.getTotalComisionesPlataforma());
        assertNotNull(rentabilidad.getTotalPagadoAVendedores());
    }
}
