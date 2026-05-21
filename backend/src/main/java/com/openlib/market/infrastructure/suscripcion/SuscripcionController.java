package com.openlib.market.infrastructure.suscripcion;

import com.openlib.market.application.suscripcion.ISeguirMiCuentaUseCase;
import com.openlib.market.application.suscripcion.SeguirMiCuentaRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class SuscripcionController {

    private final ISeguirMiCuentaUseCase seguirMiCuentaUseCase;

    public SuscripcionController(ISeguirMiCuentaUseCase seguirMiCuentaUseCase) {
        this.seguirMiCuentaUseCase = seguirMiCuentaUseCase;
    }

    @PostMapping("/{id}/suscripciones/{idVendedor}")
    public ResponseEntity<String> seguirVendedor(@PathVariable String id, @PathVariable String idVendedor) {
        SeguirMiCuentaRequestDto request = new SeguirMiCuentaRequestDto(id, idVendedor);
        seguirMiCuentaUseCase.seguir(request);
        return ResponseEntity.ok("Ahora sigues a este vendedor.");
    }
}
