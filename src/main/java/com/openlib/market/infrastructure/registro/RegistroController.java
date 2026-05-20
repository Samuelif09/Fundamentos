package com.openlib.market.infrastructure.registro;

import com.openlib.market.application.registro.IRegistrarRegistroUseCase;
import com.openlib.market.application.registro.RegistroRequestDto;
import com.openlib.market.domain.registro.EmailDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class RegistroController {

    private final IRegistrarRegistroUseCase registrarUseCase;

    public RegistroController(IRegistrarRegistroUseCase registrarUseCase) {
        this.registrarUseCase = registrarUseCase;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody RegistroRequestDto request) {
        try {
            registrarUseCase.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } catch (EmailDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
