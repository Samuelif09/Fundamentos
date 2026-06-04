package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;
import com.openlib.market.domain.vendedor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IniciarAutenticacionInteractorTest {

    private IUsuarioAuthGateway usuarioGateway;
    private IVerificadorPasswordGateway verificadorPassword;
    private ITokenGeneratorGateway tokenGenerator;
    private IVendedorGateway vendedorGateway;
    private IniciarAutenticacionInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioAuthGateway.class);
        verificadorPassword = mock(IVerificadorPasswordGateway.class);
        tokenGenerator = mock(ITokenGeneratorGateway.class);
        vendedorGateway = mock(IVendedorGateway.class);
        interactor = new IniciarAutenticacionInteractor(usuarioGateway, verificadorPassword, tokenGenerator, vendedorGateway);
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

    @Test
    void debeLanzarAccesoDenegadoSiVendedorNoEstaAprobado() {
        UsuarioAuth user = new UsuarioAuth("1", new Email("vendedor@test.com"), "hashed", "VENDEDOR");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(true);

        Vendedor vendedor = new Vendedor("vendedor-id-1", "1", new RazonSocial("Empresa"), new IdentificacionTributaria("123456789"), EstadoVerificacion.EN_REVISION);
        when(vendedorGateway.obtenerPorIdUsuario("1")).thenReturn(Optional.of(vendedor));

        LoginRequestDto req = new LoginRequestDto("vendedor@test.com", "correct");
        
        AccesoDenegadoException ex = assertThrows(AccesoDenegadoException.class, () -> interactor.iniciarSesion(req));
        assertEquals("La cuenta de vendedor está pendiente de aprobación.", ex.getMessage());
    }

    @Test
    void debeIniciarSesionSiVendedorEstaAprobado() {
        UsuarioAuth user = new UsuarioAuth("1", new Email("vendedor@test.com"), "hashed", "VENDEDOR");
        when(usuarioGateway.buscarPorEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(verificadorPassword.verificar(any(PasswordPlano.class), eq("hashed"))).thenReturn(true);
        when(tokenGenerator.generar(any(UsuarioAuth.class))).thenReturn(new TokenAcceso("jwt.token.123"));

        Vendedor vendedor = new Vendedor("vendedor-id-1", "1", new RazonSocial("Empresa"), new IdentificacionTributaria("123456789"), EstadoVerificacion.APROBADO);
        when(vendedorGateway.obtenerPorIdUsuario("1")).thenReturn(Optional.of(vendedor));

        LoginRequestDto req = new LoginRequestDto("vendedor@test.com", "correct");
        LoginResponseDto res = interactor.iniciarSesion(req);

        assertEquals("jwt.token.123", res.getToken());
    }
}
