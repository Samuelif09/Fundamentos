package com.openlib.market.infrastructure.afiliado;

import com.openlib.market.application.afiliado.ConfigurarAfiliadosRequestDto;
import com.openlib.market.application.afiliado.IConfigurarAfiliadosUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores/{idVendedor}/afiliados")
public class AfiliadosController {

    private final IConfigurarAfiliadosUseCase configurarAfiliadosUseCase;

    public AfiliadosController(IConfigurarAfiliadosUseCase configurarAfiliadosUseCase) {
        this.configurarAfiliadosUseCase = configurarAfiliadosUseCase;
    }

    @PostMapping
    public ResponseEntity<String> generarEnlaceAfiliado(
            @PathVariable String idVendedor,
            @RequestBody ConfigurarAfiliadosRequestDto request) {
        try {
            ConfigurarAfiliadosRequestDto fullRequest = new ConfigurarAfiliadosRequestDto(
                    idVendedor,
                    request.idAfiliado(),
                    request.comision()
            );
            String url = configurarAfiliadosUseCase.configurarYGenerarEnlace(fullRequest);
            return ResponseEntity.status(201).body("{\"url\": \"" + url + "\"}");
        } catch (com.openlib.market.domain.afiliado.ComisionInvalidaException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
