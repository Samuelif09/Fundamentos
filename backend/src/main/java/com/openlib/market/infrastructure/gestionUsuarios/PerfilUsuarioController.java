package com.openlib.market.infrastructure.gestionUsuarios;

import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios/me")
public class PerfilUsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/perfil")
    public Map<String, Object> getPerfil(@RequestParam(required = false) String email) {
        Map<String, Object> perfil = new HashMap<>();
        
        String nombre = "Lector Frecuente";
        String correo = "lector@ejemplo.com";
        
        if (email != null && !email.isEmpty()) {
            Optional<UsuarioEntity> optUser = usuarioRepository.findByEmail(email);
            if (optUser.isPresent()) {
                nombre = optUser.get().getNombre();
                correo = optUser.get().getEmail();
            }
        }

        perfil.put("fullName", nombre);
        perfil.put("email", correo);
        perfil.put("joinedDate", "Enero 2024");
        perfil.put("totalBooksOwned", 12);
        perfil.put("readHours", 45);
        perfil.put("favoriteGenre", "Ciencia Ficción");
        return perfil;
    }

    @GetMapping("/pedidos")
    public List<Map<String, Object>> getPedidos(@RequestParam(required = false) String email) {
        List<Map<String, Object>> pedidos = new ArrayList<>();
        
        Map<String, Object> pedido1 = new HashMap<>();
        pedido1.put("orderId", UUID.randomUUID().toString().substring(0, 8));
        pedido1.put("date", "2024-05-10");
        pedido1.put("total", 25.99);
        pedido1.put("status", "Completado");
        
        Map<String, Object> pedido2 = new HashMap<>();
        pedido2.put("orderId", UUID.randomUUID().toString().substring(0, 8));
        pedido2.put("date", "2024-05-15");
        pedido2.put("total", 14.50);
        pedido2.put("status", "Completado");

        pedidos.add(pedido1);
        pedidos.add(pedido2);

        return pedidos;
    }
}
