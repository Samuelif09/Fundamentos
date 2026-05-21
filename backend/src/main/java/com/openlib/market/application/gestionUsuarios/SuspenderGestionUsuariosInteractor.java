package com.openlib.market.application.gestionUsuarios;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.gestionUsuarios.INotificacionGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.MotivoSuspension;
import com.openlib.market.domain.registro.Usuario;

@Service
public class SuspenderGestionUsuariosInteractor implements ISuspenderGestionUsuariosUseCase {

    private final IUsuarioGateway usuarioGateway;
    private final INotificacionGateway notificacionGateway;

    public SuspenderGestionUsuariosInteractor(IUsuarioGateway usuarioGateway,
            INotificacionGateway notificacionGateway) {
        this.usuarioGateway = usuarioGateway;
        this.notificacionGateway = notificacionGateway;
    }

    @Override
    public void suspenderUsuario(String usuarioId, MotivoSuspension motivo) {
        Usuario usuario = usuarioGateway.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.suspender(motivo);

        usuarioGateway.actualizar(usuario);

        notificacionGateway.notificarSuspension(usuario.getEmail().getValor(), motivo.getRazon());
    }
}
