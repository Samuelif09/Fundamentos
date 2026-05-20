package com.openlib.market.application.suscripcion;

import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.suscripcion.ISuscripcionGateway;
import com.openlib.market.domain.suscripcion.OperacionInvalidaException;
import com.openlib.market.domain.suscripcion.SuscripcionAutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SeguirMiCuentaInteractorTest {

    private ISuscripcionGateway suscripcionGateway;
    private IUsuarioGateway usuarioGateway;
    private SeguirMiCuentaInteractor interactor;

    @BeforeEach
    void setUp() {
        suscripcionGateway = mock(ISuscripcionGateway.class);
        usuarioGateway = mock(IUsuarioGateway.class);
        interactor = new SeguirMiCuentaInteractor(suscripcionGateway, usuarioGateway);
    }

    @Test
    void debeGuardarSuscripcionSiEsVendedorValido() {
        Usuario vendedorMock = mock(Usuario.class);
        when(vendedorMock.getRol()).thenReturn(RolUsuario.VENDEDOR);
        when(usuarioGateway.buscarPorId("vendedor-1")).thenReturn(Optional.of(vendedorMock));
        when(suscripcionGateway.existeSuscripcion("comprador-1", "vendedor-1")).thenReturn(false);

        SeguirMiCuentaRequestDto request = new SeguirMiCuentaRequestDto("comprador-1", "vendedor-1");
        interactor.seguir(request);

        verify(suscripcionGateway, times(1)).guardar(any(SuscripcionAutor.class));
    }

    @Test
    void debeLanzarExcepcionSiEsMismoUsuario() {
        SeguirMiCuentaRequestDto request = new SeguirMiCuentaRequestDto("user-1", "user-1");

        assertThrows(OperacionInvalidaException.class, () -> interactor.seguir(request));
        verify(suscripcionGateway, never()).guardar(any(SuscripcionAutor.class));
    }

    @Test
    void debeLanzarExcepcionSiDestinoNoEsVendedor() {
        Usuario compradorMock = mock(Usuario.class);
        when(compradorMock.getRol()).thenReturn(RolUsuario.COMPRADOR);
        when(usuarioGateway.buscarPorId("user-2")).thenReturn(Optional.of(compradorMock));

        SeguirMiCuentaRequestDto request = new SeguirMiCuentaRequestDto("user-1", "user-2");

        assertThrows(OperacionInvalidaException.class, () -> interactor.seguir(request));
        verify(suscripcionGateway, never()).guardar(any(SuscripcionAutor.class));
    }

    @Test
    void debeLanzarExcepcionSiYaExisteSuscripcion() {
        Usuario vendedorMock = mock(Usuario.class);
        when(vendedorMock.getRol()).thenReturn(RolUsuario.VENDEDOR);
        when(usuarioGateway.buscarPorId("vendedor-1")).thenReturn(Optional.of(vendedorMock));
        when(suscripcionGateway.existeSuscripcion("comprador-1", "vendedor-1")).thenReturn(true);

        SeguirMiCuentaRequestDto request = new SeguirMiCuentaRequestDto("comprador-1", "vendedor-1");

        assertThrows(OperacionInvalidaException.class, () -> interactor.seguir(request));
        verify(suscripcionGateway, never()).guardar(any(SuscripcionAutor.class));
    }
}
