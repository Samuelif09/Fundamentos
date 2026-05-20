package com.openlib.market.application.dashboard;

import com.openlib.market.domain.dashboard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonalizarDashboardMetricasInteractorTest {

    private IConfiguracionAdminGateway configuracionGateway;
    private PersonalizarDashboardMetricasInteractor interactor;

    @BeforeEach
    void setUp() {
        configuracionGateway = mock(IConfiguracionAdminGateway.class);
        interactor = new PersonalizarDashboardMetricasInteractor(configuracionGateway);
    }

    @Test
    void debeDevolverConfiguracionVaciaSiNoExiste() {
        when(configuracionGateway.buscarPorAdminId("admin1")).thenReturn(Optional.empty());

        ConfiguracionDashboardDto dto = interactor.obtenerPreferencias("admin1");

        assertEquals("admin1", dto.getIdAdmin());
        assertTrue(dto.getWidgets().isEmpty());
    }

    @Test
    void debeGuardarNuevosWidgetsCorrectamente() {
        when(configuracionGateway.buscarPorAdminId("admin1")).thenReturn(Optional.empty());

        WidgetDto wDto = new WidgetDto("GRAFICO_VENTAS", 0, 0, 2, 2);
        
        ConfiguracionDashboardDto result = interactor.guardarPreferencias("admin1", List.of(wDto));

        assertEquals(1, result.getWidgets().size());
        assertEquals("GRAFICO_VENTAS", result.getWidgets().get(0).getTipo());
        verify(configuracionGateway).guardar(any(ConfiguracionDashboard.class));
    }

    @Test
    void debeLanzarExcepcionSiTipoWidgetEsInvalido() {
        when(configuracionGateway.buscarPorAdminId("admin1")).thenReturn(Optional.empty());

        WidgetDto wDto = new WidgetDto("TIPO_INVENTADO", 0, 0, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> 
            interactor.guardarPreferencias("admin1", List.of(wDto))
        );
    }
}
