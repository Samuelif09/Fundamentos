package com.openlib.market.application.publicacion;

import com.openlib.market.domain.almacenamiento.ArchivoVistaPrevia;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoVistaPreviaGateway;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SubirVistaPreviaInteractorTest {

    private ILibroPublicacionGateway libroGateway;
    private IAlmacenamientoVistaPreviaGateway almacenamientoGateway;
    private SubirVistaPreviaInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(ILibroPublicacionGateway.class);
        almacenamientoGateway = mock(IAlmacenamientoVistaPreviaGateway.class);
        interactor = new SubirVistaPreviaInteractor(libroGateway, almacenamientoGateway);
    }

    @Test
    void debeRechazarArchivoConMimeInvalido() {
        Libro libro = new Libro(new Isbn("123"), "T", "S", new Precio(10), "U", "C", "seller-1");
        when(libroGateway.obtenerPorIsbn("123")).thenReturn(Optional.of(libro));

        byte[] fakeExe = new byte[]{1, 2, 3};
        
        assertThrows(IllegalArgumentException.class, () -> 
            interactor.subirVistaPrevia("seller-1", "123", fakeExe, "application/x-msdownload")
        );
    }

    @Test
    void debeGuardarVistaPreviaExitosamente() {
        Libro libro = new Libro(new Isbn("123"), "T", "S", new Precio(10), "U", "C", "seller-1");
        when(libroGateway.obtenerPorIsbn("123")).thenReturn(Optional.of(libro));
        when(almacenamientoGateway.guardar(any(ArchivoVistaPrevia.class), anyString())).thenReturn("http://s3/preview_123.pdf");

        byte[] fakePdf = new byte[]{37, 80, 68, 70}; // %PDF
        
        interactor.subirVistaPrevia("seller-1", "123", fakePdf, "application/pdf");

        verify(libroGateway).actualizar(argThat(l -> "http://s3/preview_123.pdf".equals(l.getUrlVistaPrevia())));
    }
}
