package com.openlib.market.infrastructure.tienda;

import com.openlib.market.application.tienda.IPersonalizarMiTiendaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/vendedores")
public class BannerTiendaController {

    private final IPersonalizarMiTiendaUseCase personalizarMiTiendaUseCase;

    public BannerTiendaController(IPersonalizarMiTiendaUseCase personalizarMiTiendaUseCase) {
        this.personalizarMiTiendaUseCase = personalizarMiTiendaUseCase;
    }

    @PostMapping("/{idVendedor}/tienda/banner")
    public ResponseEntity<Void> subirBanner(
            @PathVariable String idVendedor,
            @RequestParam("file") MultipartFile file) {
        try {
            personalizarMiTiendaUseCase.subirBanner(
                    idVendedor,
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename()
            );
            return ResponseEntity.ok().build();
        } catch (com.openlib.market.domain.almacenamiento.ArchivoInvalidoException e) {
            return ResponseEntity.status(415).build(); // Unsupported Media Type
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
