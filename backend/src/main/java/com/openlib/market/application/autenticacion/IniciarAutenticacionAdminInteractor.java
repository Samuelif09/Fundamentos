package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;

import java.util.Optional;

/**
 * Caso de uso A-01: Autenticación de administradores.
 *
 * Regla de negocio clave (DDD): si las credenciales son correctas pero
 * el rol del usuario NO es ROLE_ADMIN, se lanza AccesoDenegadoException.
 * Esto previene la escalada de privilegios desde roles de comprador o vendedor.
 */
public class IniciarAutenticacionAdminInteractor implements IIniciarAutenticacionAdminUseCase {
 
    private final IUsuarioAuthGateway usuarioGateway;
    private final IVerificadorPasswordGateway verificadorPassword;
    private final ITokenGeneratorGateway tokenGenerator;
 
    public IniciarAutenticacionAdminInteractor(IUsuarioAuthGateway usuarioGateway,
                                               IVerificadorPasswordGateway verificadorPassword,
                                               ITokenGeneratorGateway tokenGenerator) {
        this.usuarioGateway = usuarioGateway;
        this.verificadorPassword = verificadorPassword;
        this.tokenGenerator = tokenGenerator;
    }
 
    @Override
    public LoginResponseDto iniciarSesionAdmin(LoginRequestDto request) {
        System.out.println("DEBUG ADMIN LOGIN: login attempt for email: " + request.getEmail());
        Email email;
        PasswordPlano passwordPlano;
 
        try {
            email = new Email(request.getEmail());
            passwordPlano = new PasswordPlano(request.getPassword());
        } catch (IllegalArgumentException e) {
            System.out.println("DEBUG ADMIN LOGIN: email or password format invalid");
            // Formato inválido equivale a credenciales incorrectas por seguridad
            throw new CredencialesInvalidasException();
        }
 
        // Busca en el repositorio general de usuarios (IUsuarioAuthGateway)
        Optional<UsuarioAuth> usuarioOpt = usuarioGateway.buscarPorEmail(email);
 
        if (usuarioOpt.isEmpty()) {
            System.out.println("DEBUG ADMIN LOGIN: user not found in database: " + request.getEmail());
            throw new CredencialesInvalidasException();
        }
 
        UsuarioAuth usuario = usuarioOpt.get();
 
        boolean esValida = verificadorPassword.verificar(passwordPlano, usuario.getHashContrasena());
        System.out.println("DEBUG ADMIN LOGIN: password verifying result: " + esValida);
 
        if (!esValida) {
            throw new CredencialesInvalidasException();
        }

        // Si las credenciales son correctas pero el rol del usuario NO es ADMIN, se niega el acceso
        if (!"ADMIN".equalsIgnoreCase(usuario.getRol()) && !"ROLE_ADMIN".equalsIgnoreCase(usuario.getRol())) {
            System.out.println("DEBUG ADMIN LOGIN: access denied, role is not ADMIN: " + usuario.getRol());
            throw new AccesoDenegadoException();
        }
 
        TokenAcceso token = tokenGenerator.generar(usuario);
 
        // Retorna requiereMfa = true para forzar el flujo 2FA en el frontend
        return new LoginResponseDto(token.getToken(), true, "ADMIN");
    }
}
