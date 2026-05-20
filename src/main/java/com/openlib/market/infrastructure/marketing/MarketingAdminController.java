package com.openlib.market.infrastructure.marketing;

import com.openlib.market.application.marketing.BannerDto;
import com.openlib.market.application.marketing.IGestionarBannersUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/marketing/banners")
public class MarketingAdminController {

    private final IGestionarBannersUseCase gestionarBannersUseCase;

    public MarketingAdminController(IGestionarBannersUseCase gestionarBannersUseCase) {
        this.gestionarBannersUseCase = gestionarBannersUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crearBanner(@RequestBody CrearBannerRequest request) {
        try {
            BannerDto dto = gestionarBannersUseCase.crearBanner(
                    request.titulo(),
                    request.urlImagen(),
                    request.urlDestino(),
                    request.fechaInicio(),
                    request.fechaFin()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable String id, @RequestBody CambiarEstadoRequest request) {
        try {
            gestionarBannersUseCase.cambiarEstado(id, request.estado());
            return ResponseEntity.ok("Estado del banner actualizado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BannerDto>> listarBanners() {
        return ResponseEntity.ok(gestionarBannersUseCase.listarBanners());
    }

    public record CrearBannerRequest(String titulo, String urlImagen, String urlDestino, String fechaInicio, String fechaFin) {}
    public record CambiarEstadoRequest(String estado) {}
}
