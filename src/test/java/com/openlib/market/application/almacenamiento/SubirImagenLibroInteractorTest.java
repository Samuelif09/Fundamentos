package com.openlib.market.application.almacenamiento;

import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.ArchivoInvalidoException;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubirImagenLibroInteractorTest {

    private IAlmacenamientoGateway almacenamientoGateway;
    private SubirImagenLibroInteractor interactor;

    @BeforeEach
    void setUp() {
        almacenamientoGateway = mock(IAlmacenamientoGateway.class);
        interactor = new SubirImagenLibroInteractor(almacenamientoGateway);
    }

    @Test
    void debeSubirImagenValidaYRetornarUrl() {
        byte[] bytes = new byte[100];
        ArchivoImagen archivo = new ArchivoImagen(bytes, "image/jpeg", "portada.jpg");
        when(almacenamientoGateway.guardar(any(), eq("portadas/libro-1")))
                .thenReturn("uploads/portadas/libro-1/portada.jpg");

        String url = interactor.subirPortada("libro-1", archivo);

        assertEquals("uploads/portadas/libro-1/portada.jpg", url);
        verify(almacenamientoGateway, times(1)).guardar(any(), anyString());
    }

    @Test
    void debeLanzarExcepcionSiArchivoEsDeTexto() {
        // El Value Object lanza la excepcion antes de llegar al interactor
        assertThrows(ArchivoInvalidoException.class,
                () -> new ArchivoImagen(new byte[10], "text/plain", "documento.txt"));

        verify(almacenamientoGateway, never()).guardar(any(), anyString());
    }

    @Test
    void debeLanzarExcepcionSiIdLibroEsNulo() {
        ArchivoImagen archivo = new ArchivoImagen(new byte[10], "image/png", "portada.png");
        assertThrows(IllegalArgumentException.class, () -> interactor.subirPortada(null, archivo));
    }
}
