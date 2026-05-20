package com.openlib.market.infrastructure.anomalias;

import com.openlib.market.application.anomalias.IEvaluarAnomaliaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/anomalias")
public class AnomaliasController {

    private final IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase;

    public AnomaliasController(IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase) {
        this.evaluarAnomaliaUseCase = evaluarAnomaliaUseCase;
    }

    @PostMapping("/evaluar")
    public ResponseEntity<String> evaluarManualmente() {
        evaluarAnomaliaUseCase.evaluarAnomalias();
        return ResponseEntity.ok("Evaluación de anomalías ejecutada correctamente.");
    }
}
