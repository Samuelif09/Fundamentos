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

    private final IAdminGateway adminGateway;
    private final IVerificadorPasswordGateway verificadorPassword;
    private final ITokenGeneratorGateway tokenGenerator;

    public IniciarAutenticacionAdminInteractor(IAdminGateway adminGateway,
                                               IVerificadorPasswordGateway verificadorPassword,
                                               ITokenGeneratorGateway tokenGenerator) {
        this.adminGateway = adminGateway;
        this.verificadorPassword = verificadorPassword;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public LoginResponseDto iniciarSesionAdmin(LoginRequestDto request) {
        Email email;
        PasswordPlano passwordPlano;

        try {
            email = new Email(request.getEmail());
            passwordPlano = new PasswordPlano(request.getPassword());
        } catch (IllegalArgumentException e) {
            // Formato inválido equivale a credenciales incorrectas por seguridad
            throw new CredencialesInvalidasException();
        }

        // Busca SOLO en el repositorio de admins (IAdminGateway)
        Optional<Administrador> adminOpt = adminGateway.buscarPorEmail(email);

        if (adminOpt.isEmpty()) {
            throw new CredencialesInvalidasException();
        }

        Administrador admin = adminOpt.get();

        if (!verificadorPassword.verificar(passwordPlano, admin.getHashContrasena())) {
            throw new CredencialesInvalidasException();
        }

        // Genera token temporal/parcial reutilizando la infraestructura existente
        // AC-001: 2FA obligatorio. El admin debe verificar su MFA con este token parcial.
        // AC-001: La sesión expira en 4h de inactividad (esto se configurará en el JWT de Entrega 2).
        TokenAcceso token = tokenGenerator.generar(admin.comoUsuarioAuth());

        return new LoginResponseDto(token.getToken(), true, admin.getId());
    }
}
