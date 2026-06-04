package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.domain.vendedor.EstadoVerificacion;

import java.util.Optional;


public class IniciarAutenticacionInteractor implements IIniciarAutenticacionUseCase {

    private final IUsuarioAuthGateway usuarioGateway;
    private final IVerificadorPasswordGateway verificadorPassword;
    private final ITokenGeneratorGateway tokenGenerator;
    private final IVendedorGateway vendedorGateway;

    public IniciarAutenticacionInteractor(IUsuarioAuthGateway usuarioGateway,
            IVerificadorPasswordGateway verificadorPassword,
            ITokenGeneratorGateway tokenGenerator,
            IVendedorGateway vendedorGateway) {
        this.usuarioGateway = usuarioGateway;
        this.verificadorPassword = verificadorPassword;
        this.tokenGenerator = tokenGenerator;
        this.vendedorGateway = vendedorGateway;
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

        // Bloquear cuentas con estado diferente a ACTIVO
        String estadoCuenta = usuario.getEstadoCuenta();
        if (estadoCuenta != null) {
            if ("PENDIENTE".equalsIgnoreCase(estadoCuenta)) {
                throw new AccesoDenegadoException("Tu cuenta está pendiente de aprobación por el administrador. Recibirás acceso una vez revisada tu solicitud.");
            }
            if ("SUSPENDIDO".equalsIgnoreCase(estadoCuenta) || "BANEADO".equalsIgnoreCase(estadoCuenta)) {
                throw new AccesoDenegadoException("Tu cuenta ha sido suspendida. Contacta al soporte para más información.");
            }
        }

        if ("VENDEDOR".equalsIgnoreCase(usuario.getRol())) {
            Optional<Vendedor> vendedorOpt = vendedorGateway.obtenerPorIdUsuario(usuario.getId());
            if (vendedorOpt.isEmpty() || vendedorOpt.get().getEstadoVerificacion() != EstadoVerificacion.APROBADO) {
                throw new AccesoDenegadoException("La cuenta de vendedor está pendiente de aprobación.");
            }
        }

        TokenAcceso token = tokenGenerator.generar(usuario);

        return new LoginResponseDto(token.getToken(), usuario.getRol(), usuario.getId());
    }
}
