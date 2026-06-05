package com.openlib.market.infrastructure.vendedor;

import com.openlib.market.application.vendedor.IRegistrarVendedorUseCase;
import com.openlib.market.application.vendedor.RegistrarVendedorRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/vendedores")
public class VendedorRegistroController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendedorRegistroController.class);

    private final IRegistrarVendedorUseCase registrarVendedorUseCase;

    public VendedorRegistroController(IRegistrarVendedorUseCase registrarVendedorUseCase) {
        this.registrarVendedorUseCase = registrarVendedorUseCase;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrarVendedor(@RequestBody RegistrarVendedorRequestDto request) {
        LOGGER.info("[REGISTRO_VENDEDOR] Solicitud recibida. email={}, nit={}", request.getEmail(), request.getIdentificacionTributaria());
        registrarVendedorUseCase.registrar(request);
        LOGGER.info("[REGISTRO_VENDEDOR] Registro completado. email={}, nit={}", request.getEmail(), request.getIdentificacionTributaria());
        return ResponseEntity.status(HttpStatus.CREATED).body("Vendedor registrado exitosamente.");
    }
}
