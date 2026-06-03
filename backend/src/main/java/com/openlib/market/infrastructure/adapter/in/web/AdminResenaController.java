package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.resena.IEliminarResenaUseCase;
import com.openlib.market.application.resena.IModerarResenaUseCase;
import com.openlib.market.application.resena.ModerarResenaRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/resenas")
public class AdminResenaController {

    private final IEliminarResenaUseCase eliminarResenaUseCase;
    private final IModerarResenaUseCase moderarResenaUseCase;

    public AdminResenaController(IEliminarResenaUseCase eliminarResenaUseCase, 
                                 IModerarResenaUseCase moderarResenaUseCase) {
        this.eliminarResenaUseCase = eliminarResenaUseCase;
        this.moderarResenaUseCase = moderarResenaUseCase;
    }

    @DeleteMapping("/{id_resena}")
    public ResponseEntity<Void> eliminarResena(@PathVariable("id_resena") String idResena) {
        try {
            eliminarResenaUseCase.ejecutar(idResena);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id_resena}/estado")
    public ResponseEntity<Void> moderarResena(
            @PathVariable("id_resena") String idResena, 
            @Valid @RequestBody ModerarResenaRequestDto dto) {
        try {
            moderarResenaUseCase.ejecutar(idResena, dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
