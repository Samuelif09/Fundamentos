package com.openlib.market.infrastructure.vendedor;

import com.openlib.market.application.vendedor.IRegistrarVendedorUseCase;
import com.openlib.market.application.vendedor.RegistrarVendedorRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/vendedores")
public class VendedorRegistroController {

    private final IRegistrarVendedorUseCase registrarVendedorUseCase;

    public VendedorRegistroController(IRegistrarVendedorUseCase registrarVendedorUseCase) {
        this.registrarVendedorUseCase = registrarVendedorUseCase;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrarVendedor(@RequestBody RegistrarVendedorRequestDto request) {
        registrarVendedorUseCase.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vendedor registrado exitosamente.");
    }
}
