package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.application.autenticacion.IIniciarAutenticacionAdminUseCase;
import com.openlib.market.application.autenticacion.LoginRequestDto;
import com.openlib.market.application.autenticacion.LoginResponseDto;
import com.openlib.market.domain.autenticacion.AccesoDenegadoException;
import com.openlib.market.domain.autenticacion.CredencialesInvalidasException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para A-01: Autenticación de administradores.
 * Endpoint: POST /api/v1/auth/admin/login
 */
@RequestMapping("/api/v1/auth/admin")
public class AdminAuthController {

    private final IIniciarAutenticacionAdminUseCase iniciarAutenticacionAdminUseCase;

    public AdminAuthController(IIniciarAutenticacionAdminUseCase iniciarAutenticacionAdminUseCase) {
        this.iniciarAutenticacionAdminUseCase = iniciarAutenticacionAdminUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequestDto request) {
        try {
            LoginResponseDto response = iniciarAutenticacionAdminUseCase.iniciarSesionAdmin(request);
            return ResponseEntity.ok(response);
        } catch (CredencialesInvalidasException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (AccesoDenegadoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
