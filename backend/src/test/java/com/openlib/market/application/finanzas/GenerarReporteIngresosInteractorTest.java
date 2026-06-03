package com.openlib.market.application.finanzas;
import com.openlib.market.domain.finanzas.ComisionFactory;
import com.openlib.market.domain.finanzas.IVentasReadGateway;
import com.openlib.market.domain.finanzas.VentaPlanaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class GenerarReporteIngresosInteractorTest {
    private IVentasReadGateway ventasReadGateway;
    private ComisionFactory comisionFactory;
    private GenerarReporteIngresosInteractor interactor;
    @BeforeEach
    void setUp() {
        ventasReadGateway = mock(IVentasReadGateway.class);
        comisionFactory = new ComisionFactory();
        interactor = new GenerarReporteIngresosInteractor(ventasReadGateway, comisionFactory);
    }
    @Test
    void debeCalcularTotalesCorrectamente() {
        String vendedorId = "v1";
        LocalDate desde = LocalDate.now().minusDays(10);
        LocalDate hasta = LocalDate.now();
        List<VentaPlanaDto> mockVentas = Arrays.asList(
                new VentaPlanaDto("LIBRO", new BigDecimal("100.00"), 2, java.time.LocalDateTime.now()), // 200 bruto, 20 comision
                new VentaPlanaDto("AUDIOLIBRO", new BigDecimal("50.00"), 1, java.time.LocalDateTime.now()) // 50 bruto, 10 comision
        );
        when(ventasReadGateway.obtenerVentasPorVendedorYFechas(eq(vendedorId), eq(desde), eq(hasta))).thenReturn(mockVentas);
        IngresosVendedorResponseDto resultado = interactor.ejecutar(vendedorId, desde, hasta);
        assertEquals(new BigDecimal("250.00"), resultado.getTotalVentasBrutas());
        assertEquals(new BigDecimal("30.00"), resultado.getTotalComisionesPlataforma());
        assertEquals(new BigDecimal("220.00"), resultado.getIngresoNetoVendedor());
    }
}
