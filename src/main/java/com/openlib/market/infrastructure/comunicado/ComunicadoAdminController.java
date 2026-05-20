package com.openlib.market.infrastructure.comunicado;

import com.openlib.market.application.comunicado.ComunicadoDto;
import com.openlib.market.application.comunicado.IEnviarComunicadoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/comunicados")
public class ComunicadoAdminController {

    private final IEnviarComunicadoUseCase enviarComunicadoUseCase;

    public ComunicadoAdminController(IEnviarComunicadoUseCase enviarComunicadoUseCase) {
        this.enviarComunicadoUseCase = enviarComunicadoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> enviarComunicado(@RequestBody EnviarComunicadoRequest request) {
        try {
            ComunicadoDto dto = enviarComunicadoUseCase.enviar(
                    request.asunto(), 
                    request.cuerpoMensaje(), 
                    request.filtro()
            );
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record EnviarComunicadoRequest(String asunto, String cuerpoMensaje, String filtro) {}
}
