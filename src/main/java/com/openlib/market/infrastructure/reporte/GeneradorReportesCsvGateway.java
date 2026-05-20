package com.openlib.market.infrastructure.reporte;

import com.openlib.market.domain.reporte.FormatoReporte;
import com.openlib.market.domain.reporte.IGeneradorReportesGlobalGateway;
import com.openlib.market.domain.reporte.ReportePlataforma;
import org.springframework.stereotype.Component;

@Component
public class GeneradorReportesCsvGateway implements IGeneradorReportesGlobalGateway {

    @Override
    public byte[] generarReporte(ReportePlataforma reporte, FormatoReporte formato) {
        if (formato != FormatoReporte.CSV) {
            throw new IllegalArgumentException("Solo se soporta formato CSV en esta entrega");
        }
        
        StringBuilder sb = new StringBuilder();
        // Titulo
        sb.append(reporte.getTitulo()).append("\n\n");
        
        // Cabeceras
        sb.append(String.join(",", reporte.getCabeceras())).append("\n");
        
        // Filas
        if (reporte.isVacio()) {
            sb.append("No hay datos para este reporte\n");
        } else {
            for (String[] fila : reporte.getFilas()) {
                sb.append(String.join(",", fila)).append("\n");
            }
        }
        
        return sb.toString().getBytes();
    }
}
