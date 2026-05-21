package com.openlib.market.application.registro;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.registro.*;

@Service
public class RegistrarRegistroInteractor implements IRegistrarRegistroUseCase {

    private final IRegistroGateway registroGateway;
    private final IPasswordEncoderGateway passwordEncoder;

    public RegistrarRegistroInteractor(IRegistroGateway registroGateway, IPasswordEncoderGateway passwordEncoder) {
        this.registroGateway = registroGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registrar(RegistroRequestDto request) {
        Email email = new Email(request.getEmail());
        
        if (registroGateway.existeEmail(email)) {
            throw new EmailDuplicadoException("El email ya se encuentra registrado");
        }

        Password password = new Password(request.getPassword());
        
        String hashedPassword = passwordEncoder.encode(password.getValor());

        // La contraseña ya hasheada se almacena directamente, 
        // no necesita re-validar las reglas del dominio (ya las superó antes del encode)
        Password passwordParaGuardar = Password.desdeHash(hashedPassword);

        Usuario usuario = new Usuario(
            request.getNombre(), 
            email, 
            passwordParaGuardar
        );

        registroGateway.guardar(usuario);
    }
}
