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
    private final com.openlib.market.application.biblioteca.IVerBibliotecaUseCase verBibliotecaUseCase;

    public BibliotecaController(
            IDescargarPostCompraUseCase descargarUseCase,
            com.openlib.market.application.biblioteca.IVerBibliotecaUseCase verBibliotecaUseCase) {
        this.descargarUseCase = descargarUseCase;
        this.verBibliotecaUseCase = verBibliotecaUseCase;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<java.util.List<com.openlib.market.domain.catalogo.LibroCatalogo>> listarBiblioteca(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(verBibliotecaUseCase.obtenerBibliotecaUsuario(userId));
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
            
            String filename = idLibro + ".pdf";
            if (archivo.getUrl() != null && archivo.getUrl().contains("/")) {
                filename = archivo.getUrl().substring(archivo.getUrl().lastIndexOf('/') + 1);
            }
            
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(archivo.getContenidoFisico(), headers, HttpStatus.OK);

        } catch (AccesoDenegadoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
