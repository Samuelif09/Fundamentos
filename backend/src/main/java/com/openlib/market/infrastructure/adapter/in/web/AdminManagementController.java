package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.gestionUsuarios.IAprobarGestionUsuariosUseCase;
import com.openlib.market.application.gestionUsuarios.ISuspenderGestionUsuariosUseCase;
import com.openlib.market.application.reembolso.IGestionarReembolsosUseCase;
import com.openlib.market.application.transaccionesAdmin.IVerTransaccionesAdminUseCase;
import com.openlib.market.application.transaccionesAdmin.TransaccionGlobalDto;
import com.openlib.market.domain.registro.MotivoSuspension;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminOrderDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminUserDto;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.VendedorEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminManagementController {

    private final ISuspenderGestionUsuariosUseCase suspenderUseCase;
    private final IAprobarGestionUsuariosUseCase aprobarUseCase;
    private final IVerTransaccionesAdminUseCase verTransaccionesUseCase;
    private final IGestionarReembolsosUseCase gestionarReembolsosUseCase;
    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;

    public AdminManagementController(ISuspenderGestionUsuariosUseCase suspenderUseCase,
                                     IAprobarGestionUsuariosUseCase aprobarUseCase,
                                     IVerTransaccionesAdminUseCase verTransaccionesUseCase,
                                     IGestionarReembolsosUseCase gestionarReembolsosUseCase,
                                     UsuarioRepository usuarioRepository,
                                     VendedorRepository vendedorRepository) {
        this.suspenderUseCase = suspenderUseCase;
        this.aprobarUseCase = aprobarUseCase;
        this.verTransaccionesUseCase = verTransaccionesUseCase;
        this.gestionarReembolsosUseCase = gestionarReembolsosUseCase;
        this.usuarioRepository = usuarioRepository;
        this.vendedorRepository = vendedorRepository;
    }

    /**
     * Devuelve todos los usuarios excepto administradores.
     * Orden de prioridad: SUSPENDIDO → BLOQUEADO → PENDIENTE → ACTIVO.
     */
    @GetMapping("/usuarios")
    public ResponseEntity<List<AdminUserDto>> getUsers() {
        // Prioridad de estado para mostrar primero los que requieren atención
        Map<String, Integer> prioridad = new HashMap<>();
        prioridad.put("SUSPENDIDO", 0);
        prioridad.put("SUSPENDED", 0);
        prioridad.put("BLOQUEADO", 0);
        prioridad.put("BLOCKED", 0);
        prioridad.put("PENDIENTE", 1);
        prioridad.put("PENDING", 1);
        prioridad.put("ACTIVO", 2);
        prioridad.put("ACTIVE", 2);

        List<AdminUserDto> usuarios = usuarioRepository.findAll().stream()
                // Excluir administradores
                .filter(u -> u.getRol() != null && !u.getRol().equalsIgnoreCase("A") && !u.getRol().equalsIgnoreCase("ADMIN"))
                .sorted(Comparator.comparingInt(u ->
                        prioridad.getOrDefault(u.getEstadoCuenta() != null ? u.getEstadoCuenta().toUpperCase() : "", 99)
                ))
                .map(u -> new AdminUserDto(
                        u.getId(),
                        u.getEmail(),
                        u.getNombre(),
                        u.getRol(),
                        u.getEstadoCuenta(),
                        u.getFechaRegistro() != null ? u.getFechaRegistro().toString() : ""
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/usuarios/{id}/suspender")
    public ResponseEntity<String> suspendUser(@PathVariable String id,
                                              @RequestBody(required = false) Map<String, String> body) {
        try {
            String motivo = (body != null && body.containsKey("motivo")) ? body.get("motivo") : "Incumplimiento de términos (Administración)";
            suspenderUseCase.suspenderUsuario(id, new MotivoSuspension(motivo));
            return ResponseEntity.ok("Usuario suspendido correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/{id}/reactivar")
    public ResponseEntity<String> reactivateUser(@PathVariable String id) {
        try {
            Optional<UsuarioEntity> opt = usuarioRepository.findById(id);
            if (opt.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");
            UsuarioEntity usuario = opt.get();
            usuario.setEstadoCuenta("ACTIVO");
            usuario.setMotivoSuspension(null);
            usuarioRepository.save(usuario);

            // Si es vendedor, aprobar también su perfil de verificación
            if ("VENDEDOR".equalsIgnoreCase(usuario.getRol())) {
                vendedorRepository.findByIdUsuario(id).ifPresent(vendedor -> {
                    vendedor.setEstadoVerificacion("APROBADO");
                    vendedorRepository.save(vendedor);
                });
            }

            return ResponseEntity.ok("Usuario activado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<AdminOrderDto>> getOrders() {
        List<TransaccionGlobalDto> transacciones = verTransaccionesUseCase.listarTransacciones(0, 50);

        List<AdminOrderDto> orders = transacciones.stream()
                .map(t -> new AdminOrderDto(
                        t.getIdPedido(),
                        t.getIdComprador(),
                        t.getMontoTotal(),
                        t.getEstado(),
                        t.getFecha()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/pedidos/{id}/reembolsar")
    public ResponseEntity<String> refundOrder(@PathVariable String id,
                                              @RequestBody(required = false) Map<String, String> body) {
        try {
            // El motivo se recibe pero la implementación de aprobarReembolso no lo requiere
            gestionarReembolsosUseCase.aprobarReembolso(id);
            return ResponseEntity.ok("Reembolso procesado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
