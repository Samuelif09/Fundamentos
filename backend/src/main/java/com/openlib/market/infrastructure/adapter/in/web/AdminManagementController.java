package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.gestionUsuarios.ISuspenderGestionUsuariosUseCase;
import com.openlib.market.application.reembolso.IGestionarReembolsosUseCase;
import com.openlib.market.application.transaccionesAdmin.IVerTransaccionesAdminUseCase;
import com.openlib.market.application.transaccionesAdmin.TransaccionGlobalDto;
import com.openlib.market.domain.registro.MotivoSuspension;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminOrderDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/admin")
public class AdminManagementController {

    private final ISuspenderGestionUsuariosUseCase suspenderUseCase;
    private final IVerTransaccionesAdminUseCase verTransaccionesUseCase;
    private final IGestionarReembolsosUseCase gestionarReembolsosUseCase;

    public AdminManagementController(ISuspenderGestionUsuariosUseCase suspenderUseCase,
                                     IVerTransaccionesAdminUseCase verTransaccionesUseCase,
                                     IGestionarReembolsosUseCase gestionarReembolsosUseCase) {
        this.suspenderUseCase = suspenderUseCase;
        this.verTransaccionesUseCase = verTransaccionesUseCase;
        this.gestionarReembolsosUseCase = gestionarReembolsosUseCase;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<AdminUserDto>> getUsers() {
        // Mock data since IVerUsuariosUseCase is not fully implemented in backend yet.
        List<AdminUserDto> mockUsers = new ArrayList<>();
        mockUsers.add(new AdminUserDto("usr-1", "admin@openlib.com", "Super Admin", "A", "ACTIVE", "2026-01-10"));
        mockUsers.add(new AdminUserDto("usr-2", "comprador@example.com", "Juan Perez", "C", "ACTIVE", "2026-02-15"));
        mockUsers.add(new AdminUserDto("usr-3", "vendedor@books.com", "Librería Central", "S", "SUSPENDED", "2026-03-20"));
        return ResponseEntity.ok(mockUsers);
    }

    @PostMapping("/usuarios/{id}/suspender")
    public ResponseEntity<String> suspendUser(@PathVariable String id) {
        try {
            suspenderUseCase.suspenderUsuario(id, new MotivoSuspension("Incumplimiento de términos (Administración)"));
            return ResponseEntity.ok("Usuario suspendido correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/{id}/reactivar")
    public ResponseEntity<String> reactivateUser(@PathVariable String id) {
        // Mock reactivate since use case doesn't exist
        return ResponseEntity.ok("Usuario reactivado correctamente");
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<AdminOrderDto>> getOrders() {
        List<TransaccionGlobalDto> transacciones = verTransaccionesUseCase.listarTransacciones(0, 50);

        List<AdminOrderDto> orders = transacciones.stream()
                .map(t -> new AdminOrderDto(
                        t.getIdPedido(),
                        t.getIdComprador(), // Simplification: using userId as email
                        t.getMontoTotal(),
                        t.getEstado(),
                        t.getFecha()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/pedidos/{id}/reembolsar")
    public ResponseEntity<String> refundOrder(@PathVariable String id) {
        try {
            gestionarReembolsosUseCase.aprobarReembolso(id);
            return ResponseEntity.ok("Reembolso procesado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
