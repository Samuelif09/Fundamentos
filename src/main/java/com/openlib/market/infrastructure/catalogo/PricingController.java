package com.openlib.market.infrastructure.catalogo;

import com.openlib.market.application.catalogo.ConfigurarPricingRequestDto;
import com.openlib.market.application.catalogo.IConfigurarPricingDinamicoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores/{idVendedor}/libros/{idLibro}/pricing-dinamico")
public class PricingController {

    private final IConfigurarPricingDinamicoUseCase configurarPricingDinamicoUseCase;

    public PricingController(IConfigurarPricingDinamicoUseCase configurarPricingDinamicoUseCase) {
        this.configurarPricingDinamicoUseCase = configurarPricingDinamicoUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> configurarPricing(
            @PathVariable String idVendedor,
            @PathVariable String idLibro,
            @RequestBody ConfigurarPricingRequestDto request) {
        try {
            ConfigurarPricingRequestDto fullRequest = new ConfigurarPricingRequestDto(
                    idLibro,
                    idVendedor,
                    request.precioMinimo(),
                    request.precioMaximo(),
                    request.estrategia()
            );
            configurarPricingDinamicoUseCase.configurar(fullRequest);
            return ResponseEntity.status(201).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
