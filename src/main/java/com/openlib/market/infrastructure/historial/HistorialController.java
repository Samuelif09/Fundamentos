package com.openlib.market.infrastructure.historial;

import com.openlib.market.application.historial.ItemHistorialResponseDto;
import com.openlib.market.application.historial.IVerHistorialNavegacionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class HistorialController {

    private final IVerHistorialNavegacionUseCase verHistorialUseCase;

    public HistorialController(IVerHistorialNavegacionUseCase verHistorialUseCase) {
        this.verHistorialUseCase = verHistorialUseCase;
    }

    @GetMapping("/{idUsuario}/historial-navegacion")
    public ResponseEntity<List<ItemHistorialResponseDto>> obtenerHistorial(@PathVariable String idUsuario) {
        List<ItemHistorialResponseDto> historial = verHistorialUseCase.verHistorial(idUsuario);
        return ResponseEntity.ok(historial);
    }
}
