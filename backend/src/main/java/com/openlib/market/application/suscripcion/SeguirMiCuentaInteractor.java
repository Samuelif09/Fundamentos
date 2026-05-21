package com.openlib.market.application.suscripcion;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.suscripcion.ISuscripcionGateway;
import com.openlib.market.domain.suscripcion.OperacionInvalidaException;
import com.openlib.market.domain.suscripcion.SuscripcionAutor;

import java.util.Optional;

@Service
public class SeguirMiCuentaInteractor implements ISeguirMiCuentaUseCase {

    private final ISuscripcionGateway suscripcionGateway;
    private final IUsuarioGateway usuarioGateway;

    public SeguirMiCuentaInteractor(ISuscripcionGateway suscripcionGateway, IUsuarioGateway usuarioGateway) {
        this.suscripcionGateway = suscripcionGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public void seguir(SeguirMiCuentaRequestDto request) {
        // 1. Validar que no se siga a sí mismo (también validado en el dominio)
        if (request.getIdComprador().equals(request.getIdVendedor())) {
            throw new OperacionInvalidaException("Un usuario no puede seguirse a sí mismo.");
        }

        // 2. Validar existencia y rol del vendedor
        Optional<Usuario> vendedorOpt = usuarioGateway.buscarPorId(request.getIdVendedor());
        if (vendedorOpt.isEmpty()) {
            throw new OperacionInvalidaException("El usuario destino no existe.");
        }

        Usuario vendedor = vendedorOpt.get();
        if (vendedor.getRol() != RolUsuario.VENDEDOR) {
            throw new OperacionInvalidaException("Solo se puede seguir a usuarios con rol VENDEDOR.");
        }

        // 3. Validar si ya existe la suscripción
        if (suscripcionGateway.existeSuscripcion(request.getIdComprador(), request.getIdVendedor())) {
            throw new OperacionInvalidaException("Ya sigues a este vendedor.");
        }

        // 4. Crear entidad de dominio
        SuscripcionAutor suscripcion = new SuscripcionAutor(request.getIdComprador(), request.getIdVendedor());

        // 5. Persistir
        suscripcionGateway.guardar(suscripcion);
    }
}
