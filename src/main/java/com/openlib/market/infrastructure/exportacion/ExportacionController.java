package com.openlib.market.infrastructure.exportacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.application.exportacion.IExportarMiCuentaUseCase;
import com.openlib.market.domain.exportacion.DataExportadaUsuario;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class ExportacionController {

    private final IExportarMiCuentaUseCase exportarUseCase;
    private final ObjectMapper objectMapper;

    public ExportacionController(IExportarMiCuentaUseCase exportarUseCase) {
        this.exportarUseCase = exportarUseCase;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @GetMapping("/{id}/exportar-datos")
    public ResponseEntity<String> exportarDatosPersonales(@PathVariable String id) {
        DataExportadaUsuario data = exportarUseCase.exportar(id);

        try {
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"datos_usuario_" + id + ".json\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar el archivo de exportación.");
        }
    }
}
