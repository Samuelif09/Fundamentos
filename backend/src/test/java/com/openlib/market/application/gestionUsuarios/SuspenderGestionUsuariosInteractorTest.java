package com.openlib.market.application.gestionUsuarios;

import com.openlib.market.domain.gestionUsuarios.INotificacionGateway;
import com.openlib.market.domain.registro.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("A-03: SuspenderGestionUsuariosInteractor")
class SuspenderGestionUsuariosInteractorTest {

    private IUsuarioGateway usuarioGateway;
    private INotificacionGateway notificacionGateway;
    private SuspenderGestionUsuariosInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioGateway.class);
        notificacionGateway = mock(INotificacionGateway.class);
        interactor = new SuspenderGestionUsuariosInteractor(usuarioGateway, notificacionGateway);
    }

    @Test
    @DisplayName("Debe suspender al usuario, guardarlo y enviar notificacion")
    void debeSuspenderYNotificar() {
        Usuario usuario = new Usuario("u-1", "Juan", new Email("juan@test.com"), new Password("Test1234"), RolUsuario.VISITANTE);
        when(usuarioGateway.buscarPorId("u-1")).thenReturn(Optional.of(usuario));

        MotivoSuspension motivo = new MotivoSuspension("Violación de términos");
        interactor.suspenderUsuario("u-1", motivo);

        assertEquals(EstadoCuenta.SUSPENDIDO, usuario.getEstadoCuenta());
        assertEquals(motivo, usuario.getMotivoSuspension());
        verify(usuarioGateway, times(1)).actualizar(usuario);
        verify(notificacionGateway, times(1)).notificarSuspension("juan@test.com", "Violación de términos");
    }

    @Test
    @DisplayName("Debe lanzar EstadoInvalidoException si ya está suspendido")
    void debeLanzarExcepcionSiYaSuspendido() {
        Usuario usuario = new Usuario("u-1", "Juan", new Email("juan@test.com"), new Password("Test1234"), RolUsuario.VISITANTE);
        usuario.suspender(new MotivoSuspension("Primera falta"));
        when(usuarioGateway.buscarPorId("u-1")).thenReturn(Optional.of(usuario));

        MotivoSuspension motivoNuevo = new MotivoSuspension("Segunda falta");
        assertThrows(EstadoInvalidoException.class, () -> interactor.suspenderUsuario("u-1", motivoNuevo));
        
        verify(usuarioGateway, never()).actualizar(any());
        verify(notificacionGateway, never()).notificarSuspension(anyString(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el usuario no existe")
    void debeLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioGateway.buscarPorId("u-99")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> interactor.suspenderUsuario("u-99", new MotivoSuspension("test")));
    }
}
