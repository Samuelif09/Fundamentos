package com.openlib.market.domain.autenticacion;

import java.util.Optional;

/**
 * Puerto de salida (Output Port) para buscar administradores.
 * Separado de IUsuarioAuthGateway para cumplir SRP y permitir
 * que las políticas de seguridad del admin evolucionen de forma independiente.
 */
public interface IAdminGateway {
    Optional<Administrador> buscarPorEmail(Email email);
    Optional<Administrador> buscarPorId(String id);
    void guardar(Administrador administrador);
    int contarSuperAdmins();
}
