package com.openlib.market.application.resena;

import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeerResenasInteractorTest {

    private IResenaGateway resenaGateway;
    private LeerResenasInteractor interactor;

    @BeforeEach
    void setUp() {
        resenaGateway = mock(IResenaGateway.class);
        interactor = new LeerResenasInteractor(resenaGateway);
    }

    @Test
    void debeRetornarListaVaciaSiLibroSinResenas() {
        when(resenaGateway.listarPorLibroId("isbn-1", 0, 5)).thenReturn(Collections.emptyList());

        List<ResenaResponseDto> result = interactor.leerResenas("isbn-1", 0, 5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void debeMapearResenasDomainADtoCorrectamente() {
        Resena r1 = new Resena("r-1", "isbn-1", new Calificacion(5), "Excelente libro", LocalDate.now());
        Resena r2 = new Resena("r-2", "isbn-1", new Calificacion(3), "Regular", LocalDate.now());
        when(resenaGateway.listarPorLibroId("isbn-1", 0, 5)).thenReturn(List.of(r1, r2));

        List<ResenaResponseDto> result = interactor.leerResenas("isbn-1", 0, 5);

        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getCalificacion());
        assertEquals("Excelente libro", result.get(0).getTexto());
        assertEquals(3, result.get(1).getCalificacion());
    }

    @Test
    void debeLanzarExcepcionSiIsbnEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> interactor.leerResenas(null, 0, 5));
        verify(resenaGateway, never()).listarPorLibroId(any(), anyInt(), anyInt());
    }
}
