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

    public ResenaController(IVerResenasUseCase verResenasUseCase, ILeerResenasUseCase leerResenasUseCase) {
        this.verResenasUseCase = verResenasUseCase;
        this.leerResenasUseCase = leerResenasUseCase;
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
}
