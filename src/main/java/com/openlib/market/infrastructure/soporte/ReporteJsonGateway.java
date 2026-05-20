package com.openlib.market.infrastructure.soporte;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.soporte.ElementoReportado;
import com.openlib.market.domain.soporte.EstadoReporte;
import com.openlib.market.domain.soporte.IReporteGateway;
import com.openlib.market.domain.soporte.ReporteContenido;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReporteJsonGateway implements IReporteGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ReporteDto> baseDatosEnMemoria;

    public ReporteJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("reportes.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ReporteDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardar(ReporteContenido reporte) {
        ReporteDto dto = new ReporteDto(
                reporte.getId(),
                reporte.getIdDenunciante(),
                reporte.getElementoReportado().name(),
                reporte.getIdElemento(),
                reporte.getMotivo(),
                reporte.getEstado().name()
        );
        
        // Simular upsert
        baseDatosEnMemoria.removeIf(r -> r.id().equals(reporte.getId()));
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public boolean existeReportePendiente(String idDenunciante, String idElemento) {
        return baseDatosEnMemoria.stream()
                .anyMatch(r -> r.idDenunciante().equals(idDenunciante) 
                        && r.idElemento().equals(idElemento)
                        && r.estado().equals(EstadoReporte.PENDIENTE.name()));
    }

    private record ReporteDto(String id, String idDenunciante, String elementoReportado, String idElemento, String motivo, String estado) {}
}
