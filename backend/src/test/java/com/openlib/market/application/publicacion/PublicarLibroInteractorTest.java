package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.Password;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PublicarLibroInteractorTest {

    private ILibroPublicacionGateway libroGateway;
    private IUsuarioGateway usuarioGateway;
    private IVendedorGateway vendedorGateway;
    private PublicarLibroInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(ILibroPublicacionGateway.class);
        usuarioGateway = mock(IUsuarioGateway.class);
        vendedorGateway = mock(IVendedorGateway.class);
        interactor = new PublicarLibroInteractor(libroGateway, usuarioGateway, vendedorGateway);
    }

    @Test
    void debePublicarLibroSiVendedorEsValido() {
        Vendedor vendedorMock = new Vendedor(
            "seller-1",
            "user-1",
            new RazonSocial("Libreria Uno SAS"),
            new IdentificacionTributaria("900123456-7")
        );
        Usuario usuarioVendedor = new Usuario(
            "user-1",
            "Vendedor Uno",
            new Email("vendedor1@openlib.com"),
            Password.desdeHash("hash"),
            RolUsuario.VENDEDOR
        );

        when(vendedorGateway.obtenerPorId("seller-1")).thenReturn(Optional.of(vendedorMock));
        when(usuarioGateway.buscarPorId("user-1")).thenReturn(Optional.of(usuarioVendedor));

        PublicarLibroRequestDto request = new PublicarLibroRequestDto(
                "seller-1", "ISBN-123", "Título", "Sinopsis", 10.0, "url", "Ficción"
        );

        interactor.publicar(request);

        verify(libroGateway, times(1)).guardar(any(Libro.class));
    }

    @Test
    void debeFallarSiUsuarioNoEsVendedor() {
        Vendedor vendedorMock = new Vendedor(
            "seller-2",
            "user-2",
            new RazonSocial("Libreria Dos SAS"),
            new IdentificacionTributaria("900123457-1")
        );
        Usuario comprador = new Usuario(
            "user-2",
            "Comprador Uno",
            new Email("comprador1@openlib.com"),
            Password.desdeHash("hash"),
            RolUsuario.COMPRADOR
        );

        when(vendedorGateway.obtenerPorId("seller-2")).thenReturn(Optional.of(vendedorMock));
        when(usuarioGateway.buscarPorId("user-2")).thenReturn(Optional.of(comprador));

        PublicarLibroRequestDto request = new PublicarLibroRequestDto(
            "seller-2", "ISBN-123", "Título", "Sinopsis", 10.0, "url", "Ficción"
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.publicar(request));
        verify(libroGateway, never()).guardar(any(Libro.class));
    }

    @Test
    void debeFallarSiPrecioEsNegativo() {
        Vendedor vendedorMock = new Vendedor(
            "seller-1",
            "user-1",
            new RazonSocial("Libreria Uno SAS"),
            new IdentificacionTributaria("900123456-7")
        );
        Usuario usuarioVendedor = new Usuario(
            "user-1",
            "Vendedor Uno",
            new Email("vendedor1@openlib.com"),
            Password.desdeHash("hash"),
            RolUsuario.VENDEDOR
        );

        when(vendedorGateway.obtenerPorId("seller-1")).thenReturn(Optional.of(vendedorMock));
        when(usuarioGateway.buscarPorId("user-1")).thenReturn(Optional.of(usuarioVendedor));

        PublicarLibroRequestDto request = new PublicarLibroRequestDto(
                "seller-1", "ISBN-123", "Título", "Sinopsis", -5.0, "url", "Ficción"
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.publicar(request));
        verify(libroGateway, never()).guardar(any(Libro.class));
    }
}
