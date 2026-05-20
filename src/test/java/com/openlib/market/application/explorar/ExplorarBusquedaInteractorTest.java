package com.openlib.market.application.explorar;

import com.openlib.market.domain.explorar.CriterioTendencia;
import com.openlib.market.domain.explorar.ITendenciaGateway;
import com.openlib.market.domain.explorar.LibroTendencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExplorarBusquedaInteractorTest {

    private ITendenciaGateway tendenciaGateway;
    private ExplorarBusquedaInteractor interactor;

    @BeforeEach
    void setUp() {
        tendenciaGateway = mock(ITendenciaGateway.class);
        interactor = new ExplorarBusquedaInteractor(tendenciaGateway);
    }

    @Test
    void debeRetornarTop10LibrosMasVendidos() {
        List<LibroTendencia> dummyData = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            dummyData.add(new LibroTendencia("isbn-" + i, "Libro " + i, i * 10, 4.5, LocalDate.now()));
        }
        when(tendenciaGateway.obtenerTodos()).thenReturn(dummyData);

        List<LibroTendenciaDto> resultados = interactor.explorarTendencias(CriterioTendencia.MAS_VENDIDOS);

        assertEquals(10, resultados.size());
        assertEquals("Libro 15", resultados.get(0).getTitulo()); // El que tiene 150 ventas (el mayor)
    }

    @Test
    void debeRetornarTop10LibrosMasNuevos() {
        List<LibroTendencia> dummyData = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            dummyData.add(new LibroTendencia("isbn-" + i, "Libro " + i, 10, 4.5, LocalDate.of(2023, 1, i)));
        }
        when(tendenciaGateway.obtenerTodos()).thenReturn(dummyData);

        List<LibroTendenciaDto> resultados = interactor.explorarTendencias(CriterioTendencia.NUEVOS);

        assertEquals(10, resultados.size());
        assertEquals("Libro 15", resultados.get(0).getTitulo()); // El del dia 15 de Enero
    }
}
