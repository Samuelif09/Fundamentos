package com.openlib.market.application.dashboardGlobal;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.dashboardGlobal.IDashboardGlobalGateway;
import com.openlib.market.domain.dashboardGlobal.IntervaloTiempo;
import com.openlib.market.domain.dashboardGlobal.PuntoDatos;
import com.openlib.market.domain.dashboardGlobal.SerieGraficaVentas;
import com.openlib.market.domain.pago.Pedido;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class VerDashboardMetricasInteractor implements IVerDashboardMetricasUseCase {

    private final IDashboardGlobalGateway dashboardGateway;

    public VerDashboardMetricasInteractor(IDashboardGlobalGateway dashboardGateway) {
        this.dashboardGateway = dashboardGateway;
    }

    @Override
    public SerieGraficaDto generarGraficaVentas(String intervaloStr, int anio) {
        IntervaloTiempo intervalo = IntervaloTiempo.valueOf(intervaloStr.toUpperCase());
        List<Pedido> pedidos = dashboardGateway.obtenerPedidosExitososDePlataforma(anio);

        List<PuntoDatos> puntos = List.of();
        
        if (intervalo == IntervaloTiempo.MENSUAL) {
            Map<Integer, Double> ventasPorMes = pedidos.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getFecha().getMonthValue(),
                            Collectors.summingDouble(Pedido::getTotal)
                    ));

            puntos = java.util.stream.IntStream.rangeClosed(1, 12)
                    .mapToObj(mes -> {
                        String etiqueta = Month.of(mes).getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
                        double total = ventasPorMes.getOrDefault(mes, 0.0);
                        return new PuntoDatos(etiqueta, total);
                    })
                    .collect(Collectors.toList());
        }

        SerieGraficaVentas serie = new SerieGraficaVentas(intervalo, puntos);

        List<Map<String, Object>> puntosMap = serie.getPuntos().stream()
                .map(p -> Map.of(
                        "etiqueta", (Object) p.getEtiquetaTemporal(),
                        "valor", (Object) p.getValorAcumulado()
                )).collect(Collectors.toList());

        return new SerieGraficaDto(serie.getIntervalo().name(), puntosMap, serie.getTotalAcumuladoSerie());
    }
}
