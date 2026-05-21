package com.openlib.market.application.gestionUsuarios;

import com.openlib.market.domain.registro.MotivoSuspension;

public interface ISuspenderGestionUsuariosUseCase {
    void suspenderUsuario(String usuarioId, MotivoSuspension motivo);
}
