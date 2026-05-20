package com.openlib.market.infrastructure.biblioteca;

import com.openlib.market.application.biblioteca.IDescargarPostCompraUseCase;
import com.openlib.market.domain.biblioteca.AccesoDenegadoException;
import com.openlib.market.domain.biblioteca.ArchivoDigital;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/biblioteca")
public class BibliotecaController {

    private final IDescargarPostCompraUseCase descargarUseCase;

    public BibliotecaController(IDescargarPostCompraUseCase descargarUseCase) {
        this.descargarUseCase = descargarUseCase;
    }

    @GetMapping("/{idLibro}/descargar")
    public ResponseEntity<byte[]> descargarLibro(
            @PathVariable("idLibro") String idLibro,
            @RequestParam(value = "userId", required = true) String userId) {
        try {
            // userId viene por param o token
            ArchivoDigital archivo = descargarUseCase.descargarLibro(userId, idLibro);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(archivo.getMimeType()));
            headers.setContentDispositionFormData("attachment", idLibro + ".pdf");

            return new ResponseEntity<>(archivo.getContenidoFisico(), headers, HttpStatus.OK);

        } catch (AccesoDenegadoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
