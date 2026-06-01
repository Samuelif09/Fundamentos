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
            System.out.println("DEBUG: Usuario no encontrado con email: " + email.getDireccion());
            throw new CredencialesInvalidasException();
        }

        UsuarioAuth usuario = usuarioOpt.get();

        boolean esValida = verificadorPassword.verificar(passwordPlano, usuario.getHashContrasena());
        System.out.println("DEBUG: ¿Contraseña válida?: " + esValida);


        System.out.println("DEBUG: Hash guardado en BD: " + usuario.getHashContrasena());


        if (!esValida) {
            throw new CredencialesInvalidasException();
        }

        TokenAcceso token = tokenGenerator.generar(usuario);

        return new LoginResponseDto(token.getToken(), usuario.getRol(), usuario.getId());
    }
}
