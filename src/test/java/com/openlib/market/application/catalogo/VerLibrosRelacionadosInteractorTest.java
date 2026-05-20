package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.CriterioSimilitud;
import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.LibroNoEncontradoException;
import com.openlib.market.domain.detalle.Precio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VerLibrosRelacionadosInteractorTest {

    private ICatalogoGateway catalogoGateway;
    private IDetalleGateway detalleGateway;
    private VerLibrosRelacionadosInteractor interactor;

    @BeforeEach
    void setUp() {
        catalogoGateway = mock(ICatalogoGateway.class);
        detalleGateway = mock(IDetalleGateway.class);
        interactor = new VerLibrosRelacionadosInteractor(catalogoGateway, detalleGateway);
    }

    @Test
    void debeRetornarLibrosRelacionadosSinIncluirElOriginal() {
        Libro libroBase = new Libro(new Isbn("ISBN-1"), "Clean Code", "Desc", new Precio(40.0), "url", "PROGRAMACION");
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.of(libroBase));

        // El gateway retorna 2 libros relacionados (ninguno con ISBN-1)
        when(catalogoGateway.buscarRelacionados(any(CriterioSimilitud.class))).thenReturn(List.of(
                new LibroCatalogo("ISBN-2", "Refactoring", 35.0, "url2"),
                new LibroCatalogo("ISBN-3", "Clean Architecture", 45.0, "url3")
        ));

        List<LibroCatalogoDto> relacionados = interactor.verRelacionados("ISBN-1");

        assertEquals(2, relacionados.size());
        assertTrue(relacionados.stream().noneMatch(l -> l.getIsbn().equals("ISBN-1")));
    }

    @Test
    void debeLanzarExcepcionSiLibroBaseNoExiste() {
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.empty());

        assertThrows(LibroNoEncontradoException.class, () -> interactor.verRelacionados("ISBN-X"));
        verify(catalogoGateway, never()).buscarRelacionados(any());
    }
}
