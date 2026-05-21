package com.openlib.market.application.monitoreo;

import com.openlib.market.domain.monitoreo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class EvaluarAnomaliaInteractorTest {

    private IMetricasGateway metricasGateway;
    private IAlertaNotificacionGateway notificacionGateway;
    private ReglaAnomaliaDomainService domainService;
    private EvaluarAnomaliaInteractor interactor;

    @BeforeEach
    void setUp() {
        metricasGateway = mock(IMetricasGateway.class);
        notificacionGateway = mock(IAlertaNotificacionGateway.class);
        domainService = new ReglaAnomaliaDomainService(); // Usamos la impl real del dominio
        interactor = new EvaluarAnomaliaInteractor(metricasGateway, notificacionGateway, domainService);
    }

    @Test
    void debeEnviarAlertaSiSeRompeRegla() {
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0));
        when(metricasGateway.obtenerValorActual(MetricaObjetivo.FALLOS_PAGO)).thenReturn(20.0); // 20 > 15

        interactor.evaluarReglas(List.of(regla));

        verify(notificacionGateway, times(1)).enviarAlerta(any(Alerta.class));
    }

    @Test
    void noDebeEnviarAlertaSiReglaNoSeRompe() {
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0));
        when(metricasGateway.obtenerValorActual(MetricaObjetivo.FALLOS_PAGO)).thenReturn(10.0); // 10 < 15

        interactor.evaluarReglas(List.of(regla));

        verify(notificacionGateway, never()).enviarAlerta(any(Alerta.class));
    }
}
