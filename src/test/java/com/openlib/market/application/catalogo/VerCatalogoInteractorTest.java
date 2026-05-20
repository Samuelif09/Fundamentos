package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerCatalogoInteractorTest {

    private ICatalogoGateway gateway;
    private VerCatalogoInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(ICatalogoGateway.class);
        interactor = new VerCatalogoInteractor(gateway);
    }

    @Test
    void debeRetornarCatalogoPaginadoExitosamente() {
        List<LibroCatalogo> libros = List.of(
                new LibroCatalogo("1", "Libro 1", 10.0, "url1"),
                new LibroCatalogo("2", "Libro 2", 20.0, "url2")
        );
        PaginaDominio<LibroCatalogo> paginaMock = new PaginaDominio<>(libros, 0, 2, 5);

        when(gateway.listarPaginado(any(Paginacion.class))).thenReturn(paginaMock);

        CatalogoPaginadoResponse response = interactor.verCatalogo(0, 2);

        assertEquals(2, response.getLibros().size());
        assertEquals(0, response.getPaginaActual());
        assertEquals(5, response.getTotalElementos());
        assertEquals(3, response.getTotalPaginas()); // 5 / 2 = 3 pages (0, 1, 2)
        assertTrue(response.getTieneSiguiente());
    }

    @Test
    void debeLanzarExcepcionSiPaginacionEsInvalida() {
        // El VO Paginacion falla antes de llamar al gateway
        assertThrows(IllegalArgumentException.class, () -> interactor.verCatalogo(-1, 20));
        verify(gateway, never()).listarPaginado(any());
    }
}
