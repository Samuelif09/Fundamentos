package com.openlib.market.infrastructure.gestionUsuarios;

import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class PerfilUsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ── /usuarios/me/perfil  (MeResolveFilter reescribe "me" → id real) ──
    @GetMapping("/me/perfil")
    public Map<String, Object> getPerfilMe(@RequestParam(required = false) String email) {
        return buildPerfil(null, email);
    }

    // ── /usuarios/{userId}/perfil  (ID explícito desde el frontend) ──────
    @GetMapping("/{userId}/perfil")
    public Map<String, Object> getPerfilById(@PathVariable String userId,
                                             @RequestParam(required = false) String email) {
        return buildPerfil(userId, email);
    }

    // ── /usuarios/me/pedidos ───────────────────────────────────────────────
    @GetMapping("/me/pedidos")
    public List<Map<String, Object>> getPedidosMe(@RequestParam(required = false) String email) {
        return buildPedidos(email);
    }

    // ── /usuarios/{userId}/pedidos ────────────────────────────────────────
    @GetMapping("/{userId}/pedidos")
    public List<Map<String, Object>> getPedidosById(@PathVariable String userId,
                                                     @RequestParam(required = false) String email) {
        return buildPedidos(email);
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private Map<String, Object> buildPerfil(String userId, String email) {
        Map<String, Object> perfil = new HashMap<>();

        String nombre = "Lector Frecuente";
        String correo = "lector@ejemplo.com";

        // Buscar por email (query param)
        if (email != null && !email.isEmpty()) {
            Optional<UsuarioEntity> optUser = usuarioRepository.findByEmail(email);
            if (optUser.isPresent()) {
                nombre = optUser.get().getNombre();
                correo = optUser.get().getEmail();
            }
        }
        // Buscar por ID (path variable)
        else if (userId != null && !userId.isEmpty()) {
            Optional<UsuarioEntity> optUser = usuarioRepository.findById(userId);
            if (optUser.isPresent()) {
                nombre = optUser.get().getNombre();
                correo = optUser.get().getEmail();
            }
        }

        perfil.put("fullName", nombre);
        perfil.put("email", correo);
        perfil.put("joinedDate", "Enero 2024");
        perfil.put("totalBooksOwned", 0);
        perfil.put("readHours", 0);
        perfil.put("favoriteGenre", "-");
        return perfil;
    }

    private List<Map<String, Object>> buildPedidos(String email) {
        List<Map<String, Object>> pedidos = new ArrayList<>();

        // Por ahora retorna lista vacía (sin pedidos reales aún)
        return pedidos;
    }
}
