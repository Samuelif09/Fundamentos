package com.openlib.market.infrastructure.finanzas;

import com.openlib.market.domain.finanzas.FormatoExportacion;
import com.openlib.market.domain.finanzas.IGeneradorReportesGateway;
import com.openlib.market.domain.finanzas.MatrizReporte;
import org.springframework.stereotype.Component;

@Component
public class GeneradorReportesCsvGateway implements IGeneradorReportesGateway {

    @Override
    public byte[] generar(MatrizReporte matriz, FormatoExportacion formato) {
        // En MVP solo soportamos CSV, ignoramos formato internamente pero lanzamos una pequeña validación si fuera necesario
        if (formato == FormatoExportacion.EXCEL) {
            // Un workaround para el MVP: exportamos un CSV que Excel puede abrir.
            // Para la Entrega 2, usaríamos Apache POI para devolver el binario XLSX real.
        }

        StringBuilder sb = new StringBuilder();

        // Cabeceras
        sb.append(String.join(",", matriz.getCabeceras())).append("\n");

        // Filas
        for (java.util.List<String> fila : matriz.getFilas()) {
            sb.append(String.join(",", fila)).append("\n");
        }

        return sb.toString().getBytes();
    }
}
