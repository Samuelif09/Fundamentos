package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.autenticacion.IIniciarAutenticacionAdminUseCase;
import com.openlib.market.application.autenticacion.IIniciarAutenticacionUseCase;
import com.openlib.market.application.autenticacion.LoginRequestDto;
import com.openlib.market.application.autenticacion.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IIniciarAutenticacionUseCase iniciarAutenticacionUseCase;
    private final IIniciarAutenticacionAdminUseCase iniciarAutenticacionAdminUseCase;

    public AuthController(IIniciarAutenticacionUseCase iniciarAutenticacionUseCase,
                          IIniciarAutenticacionAdminUseCase iniciarAutenticacionAdminUseCase) {
        this.iniciarAutenticacionUseCase = iniciarAutenticacionUseCase;
        this.iniciarAutenticacionAdminUseCase = iniciarAutenticacionAdminUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = iniciarAutenticacionUseCase.iniciarSesion(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponseDto> adminLogin(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = iniciarAutenticacionAdminUseCase.iniciarSesionAdmin(request);
        return ResponseEntity.ok(response);
    }
}
