package com.openlib.market.application.autenticacion;

import java.util.List;

public interface IGestionarRolesUseCase {
    void asignarRol(String adminId, String nombreRol, List<String> permisos);

    void removerRol(String adminId, String nombreRol);
}
