package com.openlib.market.application.dashboardVendedor;

import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VentaVendedorProjection;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ObtenerMetricasVendedorInteractor {

    private final PedidoRepository pedidoRepository;

    public ObtenerMetricasVendedorInteractor(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public VendedorMetricasDto obtenerMetricas(String idVendedor) {
        List<VentaVendedorProjection> ventas = pedidoRepository.findVentasPorVendedor(idVendedor);

        int totalBooksSold = 0;
        Map<String, Integer> monthlySales = new LinkedHashMap<>();

        // Inicializar los últimos 6 meses (para que la gráfica tenga datos, incluso si son 0)
        java.time.LocalDate now = java.time.LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            java.time.LocalDate monthDate = now.minusMonths(i);
            String monthName = monthDate.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            // capitalizar
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
            monthlySales.put(monthName, 0);
        }

        for (VentaVendedorProjection venta : ventas) {
            totalBooksSold += venta.getCantidad();

            // Mapear el mes de la venta a la gráfica (si corresponde a uno de los últimos 6 meses)
            String monthName = venta.getFecha().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
            
            if (monthlySales.containsKey(monthName)) {
                monthlySales.put(monthName, monthlySales.get(monthName) + venta.getCantidad());
            }
        }

        return new VendedorMetricasDto(totalBooksSold, monthlySales);
    }
}
