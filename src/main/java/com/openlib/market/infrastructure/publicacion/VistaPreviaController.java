package com.openlib.market.infrastructure.publicacion;

import com.openlib.market.application.publicacion.ISubirVistaPreviaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/vendedores")
public class VistaPreviaController {

    private final ISubirVistaPreviaUseCase subirVistaPreviaUseCase;

    public VistaPreviaController(ISubirVistaPreviaUseCase subirVistaPreviaUseCase) {
        this.subirVistaPreviaUseCase = subirVistaPreviaUseCase;
    }

    @PostMapping("/{idVendedor}/libros/{isbn}/vista-previa")
    public ResponseEntity<Void> subirVistaPrevia(
            @PathVariable String idVendedor,
            @PathVariable String isbn,
            @RequestParam("file") MultipartFile file) {
        try {
            subirVistaPreviaUseCase.subirVistaPrevia(
                    idVendedor,
                    isbn,
                    file.getBytes(),
                    file.getContentType()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build(); // 403 Forbidden
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
