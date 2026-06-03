package com.openlib.market.infrastructure.finanzas;

import com.openlib.market.application.finanzas.GenerarRentabilidadPlataformaInteractor;
import com.openlib.market.application.finanzas.RentabilidadPlataformaResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/finanzas")
public class RentabilidadAdminController {

    private final GenerarRentabilidadPlataformaInteractor generarRentabilidadPlataformaInteractor;

    public RentabilidadAdminController(GenerarRentabilidadPlataformaInteractor generarRentabilidadPlataformaInteractor) {
        this.generarRentabilidadPlataformaInteractor = generarRentabilidadPlataformaInteractor;
    }

    @GetMapping("/rentabilidad")
    public ResponseEntity<RentabilidadPlataformaResponseDto> reporteRentabilidad(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(generarRentabilidadPlataformaInteractor.ejecutar(desde, hasta));
    }
}
