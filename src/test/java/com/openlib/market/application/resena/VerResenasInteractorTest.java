package com.openlib.market.application.resena;

import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerResenasInteractorTest {

    private IResenaGateway gateway;
    private VerResenasInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IResenaGateway.class);
        interactor = new VerResenasInteractor(gateway);
    }

    @Test
    void debeRetornarResenasMapeadasADto() {
        when(gateway.buscarResenasPorIsbn("123")).thenReturn(List.of(
                new Resena("r1", "123", new Calificacion(5), "Excelente", LocalDate.now()),
                new Resena("r2", "123", new Calificacion(4), "Muy bueno", LocalDate.now()),
                new Resena("r3", "123", new Calificacion(3), "Normal", LocalDate.now())
        ));

        List<ResenaDto> resultados = interactor.verResenas("123");

        assertEquals(3, resultados.size());
        assertEquals("Excelente", resultados.get(0).getTexto());
        assertEquals(5, resultados.get(0).getCalificacion());
    }
}
