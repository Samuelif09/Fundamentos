package com.openlib.market.infrastructure.busqueda;

import com.openlib.market.application.busqueda.IBuscarBusquedaUseCase;
import com.openlib.market.application.busqueda.LibroBuscadoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
public class BusquedaController {

    private final IBuscarBusquedaUseCase buscarBusquedaUseCase;

    public BusquedaController(IBuscarBusquedaUseCase buscarBusquedaUseCase) {
        this.buscarBusquedaUseCase = buscarBusquedaUseCase;
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam("q") String keyword) {
        try {
            List<LibroBuscadoDto> resultados = buscarBusquedaUseCase.buscarPorPalabrasClave(keyword);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }
}
