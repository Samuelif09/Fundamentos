package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.application.autenticacion.IIniciarAutenticacionUseCase;
import com.openlib.market.application.autenticacion.LoginRequestDto;
import com.openlib.market.application.autenticacion.LoginResponseDto;
import com.openlib.market.domain.autenticacion.CredencialesInvalidasException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IIniciarAutenticacionUseCase iniciarAutenticacionUseCase;
    private final com.openlib.market.application.autenticacion.IRecuperarAutenticacionUseCase recuperarAutenticacionUseCase;

    public AuthController(IIniciarAutenticacionUseCase iniciarAutenticacionUseCase,
                          com.openlib.market.application.autenticacion.IRecuperarAutenticacionUseCase recuperarAutenticacionUseCase) {
        this.iniciarAutenticacionUseCase = iniciarAutenticacionUseCase;
        this.recuperarAutenticacionUseCase = recuperarAutenticacionUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            LoginResponseDto response = iniciarAutenticacionUseCase.iniciarSesion(request);
            return ResponseEntity.ok(response);
        } catch (CredencialesInvalidasException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody com.openlib.market.application.autenticacion.RecuperarPasswordRequestDto request) {
        try {
            recuperarAutenticacionUseCase.recuperarPassword(request.getEmail());
            // Siempre retorna OK incluso si no existe para evitar ataques de enumeración
            return ResponseEntity.ok("Si el correo existe en nuestro sistema, recibirá un enlace de recuperación pronto.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
