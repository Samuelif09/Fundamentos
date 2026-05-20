package com.openlib.market.infrastructure.catalogo;

import com.openlib.market.application.catalogo.IVerLibrosRelacionadosUseCase;
import com.openlib.market.application.catalogo.LibroCatalogoDto;
import com.openlib.market.domain.detalle.LibroNoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibrosRelacionadosController {

    private final IVerLibrosRelacionadosUseCase verLibrosRelacionadosUseCase;

    public LibrosRelacionadosController(IVerLibrosRelacionadosUseCase verLibrosRelacionadosUseCase) {
        this.verLibrosRelacionadosUseCase = verLibrosRelacionadosUseCase;
    }

    @GetMapping("/{isbn}/relacionados")
    public ResponseEntity<List<LibroCatalogoDto>> verRelacionados(@PathVariable("isbn") String isbn) {
        try {
            List<LibroCatalogoDto> relacionados = verLibrosRelacionadosUseCase.verRelacionados(isbn);
            return ResponseEntity.ok(relacionados);
        } catch (LibroNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
