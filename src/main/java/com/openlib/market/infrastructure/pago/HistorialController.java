package com.openlib.market.infrastructure.pago;

import com.openlib.market.application.pago.HistorialPedidoResponseDto;
import com.openlib.market.application.pago.IVerMiCuentaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class HistorialController {

    private final IVerMiCuentaUseCase verMiCuentaUseCase;

    public HistorialController(IVerMiCuentaUseCase verMiCuentaUseCase) {
        this.verMiCuentaUseCase = verMiCuentaUseCase;
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<HistorialPedidoResponseDto>> verHistorial(
            @PathVariable("id") String idUsuario,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            List<HistorialPedidoResponseDto> historial = verMiCuentaUseCase.obtenerHistorial(idUsuario, offset, limit);
            return ResponseEntity.ok(historial);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
