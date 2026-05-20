package com.openlib.market.application.antifraude;

import com.openlib.market.domain.antifraude.EvaluacionFraude;
import com.openlib.market.domain.antifraude.IAntifraudeGateway;
import com.openlib.market.domain.antifraude.MotivoAlerta;
import com.openlib.market.domain.antifraude.RiesgoTransaccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvaluarGestionVentasInteractorTest {

    private IAntifraudeGateway antifraudeGateway;
    private EvaluarGestionVentasInteractor interactor;

    @BeforeEach
    void setUp() {
        antifraudeGateway = mock(IAntifraudeGateway.class);
        interactor = new EvaluarGestionVentasInteractor(antifraudeGateway);
    }

    @Test
    void debeBloquearSiRiesgoEsExtremo() {
        EvaluacionFraude evaluacion = new EvaluacionFraude("ped1", new RiesgoTransaccion(95), MotivoAlerta.TARJETA_REPORTADA);
        when(antifraudeGateway.evaluarTransaccion("ped1", 100.0)).thenReturn(evaluacion);

        boolean resultado = interactor.evaluarTransaccion("ped1", 100.0);

        assertFalse(resultado);
    }

    @Test
    void debeAprobarSiRiesgoEsBajo() {
        EvaluacionFraude evaluacion = new EvaluacionFraude("ped1", new RiesgoTransaccion(10), MotivoAlerta.NINGUNO);
        when(antifraudeGateway.evaluarTransaccion("ped1", 50.0)).thenReturn(evaluacion);

        boolean resultado = interactor.evaluarTransaccion("ped1", 50.0);

        assertTrue(resultado);
    }
}
