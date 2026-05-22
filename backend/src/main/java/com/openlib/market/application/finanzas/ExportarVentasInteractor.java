package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExportarVentasInteractor implements IExportarVentasUseCase {

    private final ILiquidacionGateway liquidacionGateway;
    private final IGeneradorReportesGateway generadorGateway;

    public ExportarVentasInteractor(ILiquidacionGateway liquidacionGateway, IGeneradorReportesGateway generadorGateway) {
        this.liquidacionGateway = liquidacionGateway;
        this.generadorGateway = generadorGateway;
    }

    @Override
    public ReporteExportable exportar(String idVendedor, LocalDate desde, LocalDate hasta, String formatoStr) {
        FormatoExportacion formato = "excel".equalsIgnoreCase(formatoStr) ? FormatoExportacion.EXCEL : FormatoExportacion.CSV;
        Periodo periodo = new Periodo(desde, hasta);

        List<TransaccionFinanciera> transacciones = liquidacionGateway.obtenerTransaccionesPorVendedor(idVendedor).stream()
                .filter(t -> periodo.contiene(t.getFecha()))
                .toList();

        List<String> cabeceras = List.of("ID_TRANSACCION", "FECHA", "MONTO");
        List<List<String>> filas = new ArrayList<>();

        if (transacciones.isEmpty()) {
            filas.add(List.of("Sin movimientos", "-", "-"));
        } else {
            for (TransaccionFinanciera t : transacciones) {
                filas.add(List.of(t.getIdTransaccion(), t.getFecha().toString(), String.valueOf(t.getSubtotal())));
            }
        }

        MatrizReporte matriz = new MatrizReporte(cabeceras, filas);
        byte[] contenido = generadorGateway.generar(matriz, formato);

        String ext = formato == FormatoExportacion.EXCEL ? ".xlsx" : ".csv";
        String nombreArchivo = "Reporte_Ventas_" + idVendedor + "_" + desde.toString() + "_" + hasta.toString() + ext;

        return new ReporteExportable(nombreArchivo, contenido, formato);
    }
}
