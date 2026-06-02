package com.openlib.market.domain.detalle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void debeCrearLibroSiDatosSonValidos() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Precio precio = new Precio(29.99);
        Libro libro = new Libro(isbn, "Clean Code", "Una guía de código limpio.", precio, "http://portada.com/clean-code.jpg");

        assertNotNull(libro);
        assertEquals("Clean Code", libro.getTitulo());
    }

    @Test
    void debeLanzarExcepcionSiFaltanDatosObligatorios() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Precio precio = new Precio(29.99);

        assertThrows(IllegalArgumentException.class, () -> new Libro(null, "Clean Code", "Sinopsis", precio, "url"));
        assertThrows(IllegalArgumentException.class, () -> new Libro(isbn, null, "Sinopsis", precio, "url"));
        assertThrows(IllegalArgumentException.class, () -> new Libro(isbn, "", "Sinopsis", precio, "url"));
        assertThrows(IllegalArgumentException.class, () -> new Libro(isbn, "Clean Code", null, precio, "url"));
        assertThrows(IllegalArgumentException.class, () -> new Libro(isbn, "Clean Code", "Sinopsis", null, "url"));
    }

    @Test
    void debeIniciarseComoActivoPorDefecto() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Libro libro = new Libro(isbn, "Clean Code", "Sinopsis", new Precio(10.0), "url");
        assertEquals(EstadoLibro.PENDIENTE, libro.getEstado());
    }

    @Test
    void debePausarLibroExitosamente() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Libro libro = new Libro(isbn, "Clean Code", "Sinopsis", new Precio(10.0), "url").aprobar();
        
        Libro libroPausado = libro.pausar();
        
        assertEquals(EstadoLibro.PAUSADO, libroPausado.getEstado());
        // El original sigue inmutable
        assertEquals(EstadoLibro.PUBLICADO, libro.getEstado());
    }

    @Test
    void debeLanzarExcepcionAlPausarLibroYaPausado() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Libro libro = new Libro(isbn, "Clean Code", "Sinopsis", new Precio(10.0), "url").aprobar().pausar();
        
        assertThrows(com.openlib.market.domain.shared.AccionNoPermitidaException.class, libro::pausar);
    }

    @Test
    void debeLanzarExcepcionAlPausarLibroBloqueado() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Libro libro = new Libro(isbn, "Clean Code", "Sinopsis", new Precio(10.0), "url", "categoria", "vendedor", EstadoLibro.BLOQUEADO, null);
        
        assertThrows(TransicionEstadoInvalidaException.class, libro::pausar);
    }
}
