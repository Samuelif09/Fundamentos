package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PublicarContenidoDigitalInteractorTest {

    private IContenidoDigitalGateway gateway;
    private PublicarContenidoDigitalInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IContenidoDigitalGateway.class);
        interactor = new PublicarContenidoDigitalInteractor(gateway);
    }

    @Test
    void debePublicarLibroExitosamente() {
        PublicarContenidoRequestDto req = new PublicarContenidoRequestDto(
                "isbn-1", "Libro A", "Sin", 10.0, "url", "cat", "v1", TipoFormato.LIBRO, null
        );

        interactor.publicar(req);

        verify(gateway).guardarContenido(argThat(c -> c instanceof Libro));
    }

    @Test
    void debePublicarAudiolibroExitosamente() {
        PublicarContenidoRequestDto req = new PublicarContenidoRequestDto(
                "isbn-1", "Audio A", "Sin", 10.0, "url", "cat", "v1", TipoFormato.AUDIOLIBRO, 120
        );

        interactor.publicar(req);

        verify(gateway).guardarContenido(argThat(c -> c instanceof Audiolibro));
    }

    @Test
    void debeLanzarExcepcionAlPublicarAudiolibroSinDuracion() {
        PublicarContenidoRequestDto req = new PublicarContenidoRequestDto(
                "isbn-1", "Audio A", "Sin", 10.0, "url", "cat", "v1", TipoFormato.AUDIOLIBRO, null
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.publicar(req));
        verifyNoInteractions(gateway);
    }
}
