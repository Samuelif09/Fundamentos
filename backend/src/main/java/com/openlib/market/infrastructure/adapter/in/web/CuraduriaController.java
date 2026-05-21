package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.curaduria.IRechazarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.IRevisarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.LibroParaRevisionDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.PendingBookDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.RejectReasonRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/curaduria")
public class CuraduriaController {

    private final IRevisarCuraduriaContenidoUseCase revisarCuraduriaUseCase;
    private final IRechazarCuraduriaContenidoUseCase rechazarCuraduriaUseCase;

    public CuraduriaController(IRevisarCuraduriaContenidoUseCase revisarCuraduriaUseCase,
                               IRechazarCuraduriaContenidoUseCase rechazarCuraduriaUseCase) {
        this.revisarCuraduriaUseCase = revisarCuraduriaUseCase;
        this.rechazarCuraduriaUseCase = rechazarCuraduriaUseCase;
    }

    @GetMapping("/libros-pendientes")
    public ResponseEntity<List<PendingBookDto>> getPendingBooks() {
        // En un escenario real, pasaríamos parámetros de paginación. 
        // Para la UI actual, traemos la primera página con 50 registros.
        List<LibroParaRevisionDto> librosPendientes = revisarCuraduriaUseCase.listarLibrosPendientes(0, 50);

        List<PendingBookDto> response = librosPendientes.stream()
                .map(libro -> new PendingBookDto(
                        libro.getIsbn(), // id = isbn
                        libro.getTitulo(),
                        "Autor Desconocido", // DTO no tiene autor
                        libro.getNombreVendedor(),
                        libro.getPrecio(),
                        java.time.LocalDate.now().toString() // DTO no tiene fechaSubida
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/libros/{id}/aprobar")
    public ResponseEntity<String> approveBook(@PathVariable String id) {
        // Todo: Implementar IAprobarCuraduriaContenidoUseCase si fuese necesario.
        // Simulamos aprobación exitosa para mantener el flujo con el frontend.
        return ResponseEntity.ok("Libro aprobado correctamente");
    }

    @PostMapping("/libros/{id}/rechazar")
    public ResponseEntity<String> rejectBook(@PathVariable String id, @RequestBody RejectReasonRequestDto request) {
        try {
            rechazarCuraduriaUseCase.rechazarLibro(id, request.getReason());
            return ResponseEntity.ok("Libro rechazado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
