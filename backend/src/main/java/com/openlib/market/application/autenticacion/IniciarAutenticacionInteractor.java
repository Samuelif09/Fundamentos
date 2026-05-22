package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;

import java.util.Optional;


public class IniciarAutenticacionInteractor implements IIniciarAutenticacionUseCase {

    private final IUsuarioAuthGateway usuarioGateway;
    private final IVerificadorPasswordGateway verificadorPassword;
    private final ITokenGeneratorGateway tokenGenerator;

    public IniciarAutenticacionInteractor(IUsuarioAuthGateway usuarioGateway,
            IVerificadorPasswordGateway verificadorPassword,
            ITokenGeneratorGateway tokenGenerator) {
        this.usuarioGateway = usuarioGateway;
        this.verificadorPassword = verificadorPassword;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public LoginResponseDto iniciarSesion(LoginRequestDto request) {
        Email email;
        PasswordPlano passwordPlano;

        try {
            email = new Email(request.getEmail());
            passwordPlano = new PasswordPlano(request.getPassword());
        } catch (IllegalArgumentException e) {
            // Falla de formato en email o password vacío equivale a fallo de credenciales
            // para seguridad
            throw new CredencialesInvalidasException();
        }

        Optional<UsuarioAuth> usuarioOpt = usuarioGateway.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new CredencialesInvalidasException();
        }

        UsuarioAuth usuario = usuarioOpt.get();

        if (!verificadorPassword.verificar(passwordPlano, usuario.getHashContrasena())) {
            throw new CredencialesInvalidasException();
        }

        TokenAcceso token = tokenGenerator.generar(usuario);

        return new LoginResponseDto(token.getToken());
    }
}
