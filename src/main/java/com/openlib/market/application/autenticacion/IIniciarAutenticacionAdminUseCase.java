package com.openlib.market.application.autenticacion;

/**
 * Puerto de entrada (Input Port / Use Case) para la autenticación de administradores.
 * Separado de IIniciarAutenticacionUseCase para cumplir SRP y DDD.
 */
public interface IIniciarAutenticacionAdminUseCase {
    LoginResponseDto iniciarSesionAdmin(LoginRequestDto request);
}
