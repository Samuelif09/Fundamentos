package com.openlib.market.application.biblioteca;

import com.openlib.market.domain.biblioteca.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DescargarPostCompraInteractorTest {

    private IBibliotecaGateway bibliotecaGateway;
    private IAlmacenamientoGateway almacenamientoGateway;
    private DescargarPostCompraInteractor interactor;

    @BeforeEach
    void setUp() {
        bibliotecaGateway = mock(IBibliotecaGateway.class);
        almacenamientoGateway = mock(IAlmacenamientoGateway.class);
        interactor = new DescargarPostCompraInteractor(bibliotecaGateway, almacenamientoGateway);
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoTieneLicencia() {
        when(bibliotecaGateway.validarLicencia(any(LicenciaAcceso.class))).thenReturn(false);

        assertThrows(AccesoDenegadoException.class, () -> interactor.descargarLibro("user-1", "book-1"));

        verify(almacenamientoGateway, never()).recuperarArchivo(anyString());
    }

    @Test
    void debeRetornarArchivoSiTieneLicencia() {
        when(bibliotecaGateway.validarLicencia(any(LicenciaAcceso.class))).thenReturn(true);
        ArchivoDigital archivoMock = new ArchivoDigital("url", "application/pdf", new byte[]{1, 2, 3});
        when(almacenamientoGateway.recuperarArchivo("book-1")).thenReturn(Optional.of(archivoMock));

        ArchivoDigital result = interactor.descargarLibro("user-1", "book-1");

        assertNotNull(result);
        assertEquals("application/pdf", result.getMimeType());
    }
}
