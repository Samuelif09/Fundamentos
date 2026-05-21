package com.openlib.market.application.inventario;

import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerInventarioInteractorTest {

    private IInventarioGateway inventarioGateway;
    private VerInventarioInteractor interactor;

    @BeforeEach
    void setUp() {
        inventarioGateway = mock(IInventarioGateway.class);
        interactor = new VerInventarioInteractor(inventarioGateway);
    }

    @Test
    void debeListarLibrosDelVendedorCorrectamente() {
        when(inventarioGateway.listarPorVendedorId("seller-1")).thenReturn(List.of(
                new LibroCatalogo("isbn-1", "Libro A", 12.0, "url1"),
                new LibroCatalogo("isbn-2", "Libro B", 8.5, "url2")
        ));

        List<LibroInventarioDto> resultado = interactor.listarPorVendedor("seller-1");

        assertEquals(2, resultado.size());
        assertEquals("Libro A", resultado.get(0).getTitulo());
        assertEquals("seller-1", resultado.get(0).getIdVendedor());
    }

    @Test
    void debeRetornarListaVaciaSiNoTieneLibros() {
        when(inventarioGateway.listarPorVendedorId("seller-vacio")).thenReturn(List.of());

        List<LibroInventarioDto> resultado = interactor.listarPorVendedor("seller-vacio");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeFallarSiIdVendedorEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> interactor.listarPorVendedor(null));
        verify(inventarioGateway, never()).listarPorVendedorId(anyString());
    }
}
