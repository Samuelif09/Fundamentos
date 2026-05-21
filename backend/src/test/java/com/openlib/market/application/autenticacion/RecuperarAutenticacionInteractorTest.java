package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class RecuperarAutenticacionInteractorTest {

    private IUsuarioAuthGateway usuarioGateway;
    private ITokenRecuperacionGateway tokenGateway;
    private IEmailGateway emailGateway;
    private RecuperarAutenticacionInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioAuthGateway.class);
        tokenGateway = mock(ITokenRecuperacionGateway.class);
        emailGateway = mock(IEmailGateway.class);
        interactor = new RecuperarAutenticacionInteractor(usuarioGateway, tokenGateway, emailGateway);
    }

    @Test
    void debeGenerarYEnviarTokenSiElUsuarioExiste() {
        Email email = new Email("existente@test.com");
        UsuarioAuth mockUser = new UsuarioAuth("user-1", email, "hashed_pass");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(mockUser));

        assertDoesNotThrow(() -> interactor.recuperarPassword("existente@test.com"));

        verify(tokenGateway, times(1)).guardar(any(Email.class), any(TokenRecuperacion.class));
        verify(emailGateway, times(1)).enviarTokenRecuperacion(any(Email.class), any(TokenRecuperacion.class));
    }

    @Test
    void noDebeEnviarEmailNiFallarSiUsuarioNoExiste() {
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());

        // El método debe terminar sin lanzar excepción (Protección Anti-Enumeración)
        assertDoesNotThrow(() -> interactor.recuperarPassword("falso@test.com"));

        // Pero no debe guardar ni enviar nada
        verify(tokenGateway, never()).guardar(any(Email.class), any(TokenRecuperacion.class));
        verify(emailGateway, never()).enviarTokenRecuperacion(any(Email.class), any(TokenRecuperacion.class));
    }
}
