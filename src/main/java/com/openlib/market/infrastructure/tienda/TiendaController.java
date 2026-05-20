package com.openlib.market.infrastructure.tienda;

import com.openlib.market.application.tienda.ITenerMiTiendaUseCase;
import com.openlib.market.application.tienda.TiendaPublicaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tiendas")
public class TiendaController {

    private final ITenerMiTiendaUseCase tenerMiTiendaUseCase;

    public TiendaController(ITenerMiTiendaUseCase tenerMiTiendaUseCase) {
        this.tenerMiTiendaUseCase = tenerMiTiendaUseCase;
    }

    @GetMapping("/{slugTienda}")
    public ResponseEntity<TiendaPublicaDto> obtenerTiendaPublica(@PathVariable String slugTienda) {
        try {
            TiendaPublicaDto tienda = tenerMiTiendaUseCase.obtenerTienda(slugTienda);
            return ResponseEntity.ok(tienda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
