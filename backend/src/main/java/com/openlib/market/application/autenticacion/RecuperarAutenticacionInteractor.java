package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;

import java.util.Optional;

public class RecuperarAutenticacionInteractor implements IRecuperarAutenticacionUseCase {

    private final IUsuarioAuthGateway usuarioGateway;
    private final ITokenRecuperacionGateway tokenGateway;
    private final IEmailGateway emailGateway;

    public RecuperarAutenticacionInteractor(
            IUsuarioAuthGateway usuarioGateway,
            ITokenRecuperacionGateway tokenGateway,
            IEmailGateway emailGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tokenGateway = tokenGateway;
        this.emailGateway = emailGateway;
    }

    @Override
    public void recuperarPassword(String emailTexto) {
        Email email = new Email(emailTexto);

        Optional<UsuarioAuth> usuarioOpt = usuarioGateway.buscarPorEmail(email);

        // Si el usuario no existe, interceptamos la falla silenciosamente 
        // para evitar ataques de enumeración (Information Disclosure)
        if (usuarioOpt.isEmpty()) {
            return;
        }

        TokenRecuperacion token = TokenRecuperacion.generarNuevo();
        
        tokenGateway.guardar(email, token);
        emailGateway.enviarTokenRecuperacion(email, token);
    }
}
