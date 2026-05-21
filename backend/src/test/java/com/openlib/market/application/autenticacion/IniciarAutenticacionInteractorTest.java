package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IniciarAutenticacionInteractorTest {

    private IUsuarioAuthGateway usuarioGateway;
    private IVerificadorPasswordGateway verificadorPassword;
    private ITokenGeneratorGateway tokenGenerator;
    private IniciarAutenticacionInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioAuthGateway.class);
        verificadorPassword = mock(IVerificadorPasswordGateway.class);
        tokenGenerator = mock(ITokenGeneratorGateway.class);
        interactor = new IniciarAutenticacionInteractor(usuarioGateway, verificadorPassword, tokenGenerator);
    }

    @Test
    void debeLanzarExcepcionSiEmailNoExiste() {
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());

        LoginRequestDto req = new LoginRequestDto("test@test.com", "12345");
        assertThrows(CredencialesInvalidasException.class, () -> interactor.iniciarSesion(req));
    }

    @Test
    void debeLanzarExcepcionSiPasswordIncorrecto() {
        UsuarioAuth user = new UsuarioAuth("1", new Email("test@test.com"), "hashed");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(false);

        LoginRequestDto req = new LoginRequestDto("test@test.com", "wrong");
        assertThrows(CredencialesInvalidasException.class, () -> interactor.iniciarSesion(req));
    }

    @Test
    void debeRetornarTokenSiCredencialesValidas() {
        UsuarioAuth user = new UsuarioAuth("1", new Email("test@test.com"), "hashed");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(true);
        when(tokenGenerator.generar(any(UsuarioAuth.class))).thenReturn(new TokenAcceso("jwt.token.123"));

        LoginRequestDto req = new LoginRequestDto("test@test.com", "correct");
        LoginResponseDto res = interactor.iniciarSesion(req);

        assertEquals("jwt.token.123", res.getToken());
    }
}
