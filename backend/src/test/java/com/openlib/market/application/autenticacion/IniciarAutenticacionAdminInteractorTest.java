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

    private IAdminGateway adminGateway;
    private IVerificadorPasswordGateway verificadorPassword;
    private ITokenGeneratorGateway tokenGenerator;
    private IniciarAutenticacionAdminInteractor interactor;

    @BeforeEach
    void setUp() {
        adminGateway = mock(IAdminGateway.class);
        verificadorPassword = mock(IVerificadorPasswordGateway.class);
        tokenGenerator = mock(ITokenGeneratorGateway.class);
        interactor = new IniciarAutenticacionAdminInteractor(adminGateway, verificadorPassword, tokenGenerator);
    }

    @Test
    @DisplayName("Debe lanzar CredencialesInvalidasException si el email no existe en admins")
    void debeLanzarExcepcionSiEmailNoExiste() {
        when(adminGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.empty());

        LoginRequestDto req = new LoginRequestDto("nadie@openlib.com", "123456");
        assertThrows(CredencialesInvalidasException.class,
                () -> interactor.iniciarSesionAdmin(req));
    }

    @Test
    @DisplayName("Debe lanzar CredencialesInvalidasException si la contraseña es incorrecta")
    void debeLanzarExcepcionSiPasswordIncorrecto() {
        Administrador admin = new Administrador("a-1", new Email("admin@openlib.com"), "hashed", Rol.ROLE_ADMIN);
        when(adminGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(admin));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(false);

        LoginRequestDto req = new LoginRequestDto("admin@openlib.com", "wrongpass");
        assertThrows(CredencialesInvalidasException.class,
                () -> interactor.iniciarSesionAdmin(req));
    }

    @Test
    @DisplayName("Debe retornar token JWT si las credenciales de admin son válidas")
    void debeRetornarTokenSiCredencialesAdminValidas() {
        Administrador admin = new Administrador("a-1", new Email("admin@openlib.com"), "hashed", Rol.ROLE_ADMIN);
        when(adminGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(admin));
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
    @DisplayName("Debe lanzar CredencialesInvalidasException si el email tiene formato inválido")
    void debeLanzarExcepcionSiEmailFormatoInvalido() {
        LoginRequestDto req = new LoginRequestDto("no-es-un-email", "123456");
        assertThrows(CredencialesInvalidasException.class,
                () -> interactor.iniciarSesionAdmin(req));
    }
}
