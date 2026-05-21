package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PublicarLibroInteractorTest {

    private ILibroPublicacionGateway libroGateway;
    private IUsuarioGateway usuarioGateway;
    private PublicarLibroInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(ILibroPublicacionGateway.class);
        usuarioGateway = mock(IUsuarioGateway.class);
        interactor = new PublicarLibroInteractor(libroGateway, usuarioGateway);
    }

    @Test
    void debePublicarLibroSiVendedorEsValido() {
        Usuario vendedorMock = mock(Usuario.class);
        when(vendedorMock.getRol()).thenReturn(RolUsuario.VENDEDOR);
        when(usuarioGateway.buscarPorId("seller-1")).thenReturn(Optional.of(vendedorMock));

        PublicarLibroRequestDto request = new PublicarLibroRequestDto(
                "seller-1", "ISBN-123", "Título", "Sinopsis", 10.0, "url", "Ficción"
        );

        interactor.publicar(request);

        verify(libroGateway, times(1)).guardar(any(Libro.class));
    }

    @Test
    void debeFallarSiUsuarioNoEsVendedor() {
        Usuario compradorMock = mock(Usuario.class);
        when(compradorMock.getRol()).thenReturn(RolUsuario.COMPRADOR);
        when(usuarioGateway.buscarPorId("buyer-1")).thenReturn(Optional.of(compradorMock));

        PublicarLibroRequestDto request = new PublicarLibroRequestDto(
                "buyer-1", "ISBN-123", "Título", "Sinopsis", 10.0, "url", "Ficción"
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.publicar(request));
        verify(libroGateway, never()).guardar(any(Libro.class));
    }

    @Test
    void debeFallarSiPrecioEsNegativo() {
        Usuario vendedorMock = mock(Usuario.class);
        when(vendedorMock.getRol()).thenReturn(RolUsuario.VENDEDOR);
        when(usuarioGateway.buscarPorId("seller-1")).thenReturn(Optional.of(vendedorMock));

        PublicarLibroRequestDto request = new PublicarLibroRequestDto(
                "seller-1", "ISBN-123", "Título", "Sinopsis", -5.0, "url", "Ficción"
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.publicar(request));
        verify(libroGateway, never()).guardar(any(Libro.class));
    }
}
