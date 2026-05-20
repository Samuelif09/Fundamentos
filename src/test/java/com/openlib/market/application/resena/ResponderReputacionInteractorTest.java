package com.openlib.market.application.resena;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;
import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.RespuestaDuplicadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResponderReputacionInteractorTest {

    private IResenaGateway resenaGateway;
    private ILibroPublicacionGateway libroGateway;
    private ResponderReputacionInteractor interactor;

    @BeforeEach
    void setUp() {
        resenaGateway = mock(IResenaGateway.class);
        libroGateway = mock(ILibroPublicacionGateway.class);
        interactor = new ResponderReputacionInteractor(resenaGateway, libroGateway);
    }

    @Test
    void debeAdjuntarRespuestaExitosamente() {
        Resena resena = new Resena("r1", "isbn-1", new Calificacion(5), "Excelente", LocalDate.now());
        Libro libro = new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1");

        when(resenaGateway.obtenerPorId("r1")).thenReturn(Optional.of(resena));
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(libro));

        interactor.responder("seller-1", "r1", "Gracias por tu reseña");

        verify(resenaGateway).actualizar(argThat(r -> r.getRespuestaVendedor() != null));
    }

    @Test
    void debeLanzarExcepcionSiVendedorAjenoIntentaResponder() {
        Resena resena = new Resena("r1", "isbn-1", new Calificacion(5), "Excelente", LocalDate.now());
        Libro libro = new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1");

        when(resenaGateway.obtenerPorId("r1")).thenReturn(Optional.of(resena));
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(libro));

        assertThrows(IllegalStateException.class, () ->
                interactor.responder("hacker-99", "r1", "Respuesta no autorizada")
        );
        verify(resenaGateway, never()).actualizar(any());
    }

    @Test
    void debeLanzarExcepcionSiYaHayRespuesta() {
        Resena resena = new Resena("r1", "isbn-1", new Calificacion(5), "Excelente", LocalDate.now());
        resena.responder(new com.openlib.market.domain.resena.ComentarioRespuesta("Primera respuesta"));

        Libro libro = new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1");

        when(resenaGateway.obtenerPorId("r1")).thenReturn(Optional.of(resena));
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(libro));

        assertThrows(RespuestaDuplicadaException.class, () ->
                interactor.responder("seller-1", "r1", "Segunda respuesta")
        );
    }
}
