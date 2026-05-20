package com.openlib.market.application.curaduria;

import com.openlib.market.domain.curaduria.IInteligenciaArtificialGateway;
import com.openlib.market.domain.curaduria.ScoreToxicidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DetectarCuraduriaContenidoInteractorTest {

    private IInteligenciaArtificialGateway iaGateway;
    private DetectarCuraduriaContenidoInteractor interactor;

    @BeforeEach
    void setUp() {
        iaGateway = mock(IInteligenciaArtificialGateway.class);
        interactor = new DetectarCuraduriaContenidoInteractor(iaGateway);
    }

    @Test
    void debeRetornarRechazadoParaTextoToxico() {
        when(iaGateway.analizarTexto("texto muy malo")).thenReturn(new ScoreToxicidad(0.95));

        String resultado = interactor.evaluarContenido("elem1", "texto muy malo");

        assertEquals("RECHAZADO", resultado);
    }
    
    @Test
    void debeRetornarAprobadoParaTextoBueno() {
        when(iaGateway.analizarTexto("texto bueno")).thenReturn(new ScoreToxicidad(0.1));

        String resultado = interactor.evaluarContenido("elem1", "texto bueno");

        assertEquals("APROBADO", resultado);
    }
}
