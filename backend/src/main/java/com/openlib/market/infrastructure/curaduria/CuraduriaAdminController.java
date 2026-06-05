package com.openlib.market.infrastructure.curaduria;

import com.openlib.market.application.curaduria.IRechazarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.IRevisarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.LibroParaRevisionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/admin/curaduria")
public class CuraduriaAdminController {

    private final IRevisarCuraduriaContenidoUseCase revisarUseCase;
    private final IRechazarCuraduriaContenidoUseCase rechazarUseCase;

    public CuraduriaAdminController(IRevisarCuraduriaContenidoUseCase revisarUseCase, IRechazarCuraduriaContenidoUseCase rechazarUseCase) {
        this.revisarUseCase = revisarUseCase;
        this.rechazarUseCase = rechazarUseCase;
    }

    @GetMapping("/libros-pendientes")
    public ResponseEntity<List<LibroParaRevisionDto>> listarLibrosPendientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<LibroParaRevisionDto> libros = revisarUseCase.listarLibrosPendientes(page, size);
        return ResponseEntity.ok(libros);
    }

    @PostMapping("/libros/{bookId}/rechazar")
    public ResponseEntity<String> rechazarLibro(@PathVariable String bookId, @RequestBody RechazoRequest request) {
        try {
            rechazarUseCase.rechazarLibro(bookId, request.motivo());
            return ResponseEntity.ok("Libro rechazado correctamente");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public record RechazoRequest(String motivo) {}
}
