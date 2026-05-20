package com.openlib.market.application.busqueda;

import com.openlib.market.domain.busqueda.IBusquedaGateway;
import com.openlib.market.domain.busqueda.LibroBuscado;
import com.openlib.market.domain.busqueda.PalabraClave;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarBusquedaInteractorTest {

    private IBusquedaGateway gateway;
    private BuscarBusquedaInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IBusquedaGateway.class);
        interactor = new BuscarBusquedaInteractor(gateway);
    }

    @Test
    void debeRetornarListaDeDtosCuandoBusquedaEsExitosa() {
        // Arrange
        String query = "spring";
        when(gateway.buscarPorPalabraClave(any(PalabraClave.class)))
            .thenReturn(List.of(new LibroBuscado("1", "Spring Boot In Action", "Craig Walls")));

        // Act
        List<LibroBuscadoDto> resultados = interactor.buscarPorPalabrasClave(query);

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Spring Boot In Action", resultados.get(0).getTitulo());
        verify(gateway, times(1)).buscarPorPalabraClave(any(PalabraClave.class));
    }
    
    @Test
    void debePropagarExcepcionSiPalabraEsInvalida() {
        // Arrange
        String queryInvalido = "a";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> interactor.buscarPorPalabrasClave(queryInvalido));
        verify(gateway, never()).buscarPorPalabraClave(any(PalabraClave.class));
    }
}
