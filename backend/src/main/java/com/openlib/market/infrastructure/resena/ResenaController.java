package com.openlib.market.infrastructure.resena;

import com.openlib.market.application.resena.ILeerResenasUseCase;
import com.openlib.market.application.resena.IVerResenasUseCase;
import com.openlib.market.application.resena.ResenaDto;
import com.openlib.market.application.resena.ResenaResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class ResenaController {

    private final IVerResenasUseCase verResenasUseCase;
    private final ILeerResenasUseCase leerResenasUseCase;
    private final com.openlib.market.application.resena.IAgregarResenaUseCase agregarResenaUseCase;

    public ResenaController(IVerResenasUseCase verResenasUseCase, 
                            ILeerResenasUseCase leerResenasUseCase,
                            com.openlib.market.application.resena.IAgregarResenaUseCase agregarResenaUseCase) {
        this.verResenasUseCase = verResenasUseCase;
        this.leerResenasUseCase = leerResenasUseCase;
        this.agregarResenaUseCase = agregarResenaUseCase;
    }

    @GetMapping("/{id}/resenas")
    public ResponseEntity<List<ResenaDto>> verResenas(@PathVariable("id") String id) {
        List<ResenaDto> resenas = verResenasUseCase.verResenas(id);
        return ResponseEntity.ok(resenas);
    }

    @GetMapping("/{id}/resenas/paginadas")
    public ResponseEntity<List<ResenaResponseDto>> leerResenasPaginadas(
            @PathVariable("id") String id,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        try {
            List<ResenaResponseDto> resenas = leerResenasUseCase.leerResenas(id, offset, limit);
            return ResponseEntity.ok(resenas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{isbn}/resenas")
    public ResponseEntity<Void> agregarResena(
            @PathVariable("isbn") String isbn,
            @RequestBody com.openlib.market.application.resena.AgregarResenaRequestDto request) {
        try {
            agregarResenaUseCase.ejecutar(isbn, request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
