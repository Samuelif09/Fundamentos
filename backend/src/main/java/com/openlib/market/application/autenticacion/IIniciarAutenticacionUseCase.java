package com.openlib.market.application.autenticacion;

public interface IIniciarAutenticacionUseCase {
    LoginResponseDto iniciarSesion(LoginRequestDto request);
}
