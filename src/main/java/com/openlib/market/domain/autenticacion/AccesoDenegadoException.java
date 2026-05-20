package com.openlib.market.domain.autenticacion;

/**
 * Se lanza cuando un usuario sin privilegios de administrador
 * intenta autenticarse en el panel de administración.
 */
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException() {
        super("Acceso denegado: se requiere rol ADMIN para este recurso.");
    }
}
