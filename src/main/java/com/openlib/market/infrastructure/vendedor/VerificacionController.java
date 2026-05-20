package com.openlib.market.infrastructure.vendedor;

import com.openlib.market.application.vendedor.IVerificarRegistroVendedorUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/vendedores")
public class VerificacionController {

    private final IVerificarRegistroVendedorUseCase verificarRegistroVendedorUseCase;

    public VerificacionController(IVerificarRegistroVendedorUseCase verificarRegistroVendedorUseCase) {
        this.verificarRegistroVendedorUseCase = verificarRegistroVendedorUseCase;
    }

    @PostMapping("/{idVendedor}/verificacion")
    public ResponseEntity<Void> solicitarVerificacion(
            @PathVariable String idVendedor,
            @RequestParam("file") MultipartFile file) {
        try {
            verificarRegistroVendedorUseCase.solicitarVerificacion(
                    idVendedor,
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename()
            );
            return ResponseEntity.ok().build();
        } catch (com.openlib.market.domain.almacenamiento.ArchivoInvalidoException e) {
            return ResponseEntity.status(415).build(); // Unsupported Media Type
        } catch (com.openlib.market.domain.vendedor.VerificacionEnCursoException | com.openlib.market.domain.vendedor.VendedorYaVerificadoException e) {
            return ResponseEntity.status(409).build(); // Conflict
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
