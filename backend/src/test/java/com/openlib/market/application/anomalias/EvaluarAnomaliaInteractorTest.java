package com.openlib.market.application.anomalias;

import com.openlib.market.domain.anomalias.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvaluarAnomaliaInteractorTest {

    private IMetricasGateway metricasGateway;
    private INotificacionGateway notificacionGateway;
    private ReglaAnomaliaDomainService domainService;
    private EvaluarAnomaliaInteractor interactor;

    @BeforeEach
    void setUp() {
        metricasGateway = mock(IMetricasGateway.class);
        notificacionGateway = mock(INotificacionGateway.class);
        domainService = new ReglaAnomaliaDomainService();
        interactor = new EvaluarAnomaliaInteractor(metricasGateway, notificacionGateway, domainService);
    }

    @Test
    void debeCrearYEnviarAlertaCuandoMetricaSuperaUmbral() {
        // Arrange
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0));
        when(metricasGateway.obtenerReglasActivas()).thenReturn(List.of(regla));
        when(metricasGateway.obtenerValorActualMetrica(MetricaObjetivo.FALLOS_PAGO)).thenReturn(20.0); // 20% fallos

        // Act
        interactor.evaluarAnomalias();

        // Assert
        ArgumentCaptor<Alerta> alertaCaptor = ArgumentCaptor.forClass(Alerta.class);
        verify(notificacionGateway, times(1)).enviarAlerta(alertaCaptor.capture());

        Alerta alertaEnviada = alertaCaptor.getValue();
        assertNotNull(alertaEnviada);
        assertEquals(EstadoAlerta.CRITICO, alertaEnviada.getEstado());
        assertEquals(regla, alertaEnviada.getReglaInfringida());
        assertEquals(20.0, alertaEnviada.getValorRegistrado());
    }

    @Test
    void noDebeEnviarAlertaCuandoMetricaEsNormal() {
        // Arrange
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0));
        when(metricasGateway.obtenerReglasActivas()).thenReturn(List.of(regla));
        when(metricasGateway.obtenerValorActualMetrica(MetricaObjetivo.FALLOS_PAGO)).thenReturn(10.0); // 10% fallos

        // Act
        interactor.evaluarAnomalias();

        // Assert
        verify(notificacionGateway, never()).enviarAlerta(any(Alerta.class));
    }
}
