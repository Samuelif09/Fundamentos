package com.openlib.market.infrastructure.transaccionesAdmin;

import com.openlib.market.application.transaccionesAdmin.IVerTransaccionesAdminUseCase;
import com.openlib.market.application.transaccionesAdmin.TransaccionGlobalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/ventas")
public class TransaccionesAdminController {

    private final IVerTransaccionesAdminUseCase verTransaccionesUseCase;

    public TransaccionesAdminController(IVerTransaccionesAdminUseCase verTransaccionesUseCase) {
        this.verTransaccionesUseCase = verTransaccionesUseCase;
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionGlobalDto>> listarTransacciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<TransaccionGlobalDto> transacciones = verTransaccionesUseCase.listarTransacciones(page, size);
        return ResponseEntity.ok(transacciones);
    }
}
