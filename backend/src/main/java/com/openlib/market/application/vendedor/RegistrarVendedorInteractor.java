package com.openlib.market.application.vendedor;

import com.openlib.market.domain.registro.*;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;

public class RegistrarVendedorInteractor implements IRegistrarVendedorUseCase {

    private final IRegistroGateway registroGateway;
    private final IVendedorGateway vendedorGateway;
    private final IPasswordEncoderGateway passwordEncoder;

    public RegistrarVendedorInteractor(IRegistroGateway registroGateway, 
                                       IVendedorGateway vendedorGateway, 
                                       IPasswordEncoderGateway passwordEncoder) {
        this.registroGateway = registroGateway;
        this.vendedorGateway = vendedorGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registrar(RegistrarVendedorRequestDto request) {
        // 1. Validar identificacion tributaria (Value Object valida formato, Gateway valida existencia)
        IdentificacionTributaria identTrib = new IdentificacionTributaria(request.getIdentificacionTributaria());
        if (vendedorGateway.existePorIdentificacionTributaria(identTrib.getValor())) {
            throw new IllegalArgumentException("La identificación tributaria ya está registrada.");
        }

        // 2. Crear y validar email y password
        Email email = new Email(request.getEmail());
        if (registroGateway.existeEmail(email)) {
            throw new EmailDuplicadoException("El email ya se encuentra registrado");
        }

        Password password = new Password(request.getPassword());
        String hashedPassword = passwordEncoder.encode(password.getValor());
        Password passwordParaGuardar = Password.desdeHash(hashedPassword);

        // 3. Crear el Usuario base, forzando rol VENDEDOR
        // Usamos el constructor completo para setear el rol directamente
        Usuario usuario = new Usuario(
            java.util.UUID.randomUUID().toString(),
            request.getNombre(), 
            email, 
            passwordParaGuardar,
            RolUsuario.VENDEDOR
        );

        // 4. Crear el perfil de Vendedor asociado
        RazonSocial razonSocial = new RazonSocial(request.getRazonSocial());
        Vendedor vendedor = new Vendedor(usuario.getId(), razonSocial, identTrib);

        // 5. Persistir (Idealmente transaccional)
        registroGateway.guardar(usuario);
        vendedorGateway.guardar(vendedor);
    }
}
