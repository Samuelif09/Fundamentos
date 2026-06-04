package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests TDD para A-01: Autenticación de administradores.
 * Valida el aislamiento de roles y la escalada de privilegios.
 */
@DisplayName("A-01: IniciarAutenticacionAdminInteractor")
class IniciarAutenticacionAdminInteractorTest {

    private IUsuarioAuthGateway usuarioGateway;
    private IVerificadorPasswordGateway verificadorPassword;
    private ITokenGeneratorGateway tokenGenerator;
    private IniciarAutenticacionAdminInteractor interactor;
 
    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioAuthGateway.class);
        verificadorPassword = mock(IVerificadorPasswordGateway.class);
        tokenGenerator = mock(ITokenGeneratorGateway.class);
        interactor = new IniciarAutenticacionAdminInteractor(usuarioGateway, verificadorPassword, tokenGenerator);
    }
 
    @Test
    @DisplayName("Debe lanzar CredencialesInvalidasException si el email no existe en la base de datos")
    void debeLanzarExcepcionSiEmailNoExiste() {
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());
 
        LoginRequestDto req = new LoginRequestDto("nadie@openlib.com", "123456");
        assertThrows(CredencialesInvalidasException.class,
                 () -> interactor.iniciarSesionAdmin(req));
    }
 
    @Test
    @DisplayName("Debe lanzar CredencialesInvalidasException si la contraseña es incorrecta")
    void debeLanzarExcepcionSiPasswordIncorrecto() {
        UsuarioAuth usuario = new UsuarioAuth("a-1", new Email("admin@openlib.com"), "hashed", "ADMIN");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(usuario));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(false);
 
        LoginRequestDto req = new LoginRequestDto("admin@openlib.com", "wrongpass");
        assertThrows(CredencialesInvalidasException.class,
                 () -> interactor.iniciarSesionAdmin(req));
    }
 
    @Test
    @DisplayName("Debe retornar token JWT si las credenciales de admin son válidas")
    void debeRetornarTokenSiCredencialesAdminValidas() {
        UsuarioAuth usuario = new UsuarioAuth("a-1", new Email("admin@openlib.com"), "hashed", "ADMIN");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(usuario));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(true);
        when(tokenGenerator.generar(any(UsuarioAuth.class))).thenReturn(new TokenAcceso("jwt.admin.token"));
 
        LoginRequestDto req = new LoginRequestDto("admin@openlib.com", "correctpass");
        LoginResponseDto res = interactor.iniciarSesionAdmin(req);
 
        assertEquals("jwt.admin.token", res.getToken());
    }

    @Test
    @DisplayName("AccesoDenegadoException si se intenta crear Administrador con rol BUYER (escalada de privilegios)")
    void debeLanzarAccesoDenegadoSiRolNOEsAdmin() {
        // Este test valida la invariante de dominio del agregado Administrador:
        // construir con ROLE_BUYER debe fallar en el dominio mismo.
        assertThrows(AccesoDenegadoException.class, () ->
                new Administrador("u-1", new Email("comprador@openlib.com"), "hash", Rol.ROLE_BUYER)
        );
    }

    @Test
    @DisplayName("AccesoDenegadoException si el usuario tiene credenciales correctas pero rol COMPRADOR")
    void debeLanzarAccesoDenegadoSiUsuarioTieneRolComprador() {
        UsuarioAuth usuario = new UsuarioAuth("u-1", new Email("comprador@openlib.com"), "hashed", "COMPRADOR");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(usuario));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(true);

        LoginRequestDto req = new LoginRequestDto("comprador@openlib.com", "correctpass");
        assertThrows(AccesoDenegadoException.class,
                () -> interactor.iniciarSesionAdmin(req));
    }

    @Test
    @DisplayName("Debe lanzar CredencialesInvalidasException si el email tiene formato inválido")
    void debeLanzarExcepcionSiEmailFormatoInvalido() {
        LoginRequestDto req = new LoginRequestDto("no-es-un-email", "123456");
        assertThrows(CredencialesInvalidasException.class,
                () -> interactor.iniciarSesionAdmin(req));
    }
}
