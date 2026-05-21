package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.dashboardGlobal.IDashboardGlobalGateway;
import com.openlib.market.domain.dashboardGlobal.IntervaloTiempo;
import com.openlib.market.domain.dashboardGlobal.PuntoDatos;
import com.openlib.market.domain.dashboardGlobal.SerieGraficaVentas;
import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.PedidoMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Primary
public class GraficaVentasJpaGateway implements IDashboardGlobalGateway {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public GraficaVentasJpaGateway(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Override
    public List<Pedido> obtenerPedidosExitososDePlataforma(int anio) {
        return pedidoRepository.findAll().stream()
                .filter(e -> EstadoPedido.PAGADO.name().equals(e.getEstado()))
                .filter(e -> e.getFecha() != null && e.getFecha().getYear() == anio)
                .map(pedidoMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Agrupa los pedidos PAGADOS del año dado en intervalos (DIARIO, SEMANAL, MENSUAL).
     * Retorna una SerieGraficaVentas con puntos de datos acumulados por cada intervalo.
     */
    public SerieGraficaVentas generarSerieVentas(int anio, IntervaloTiempo intervalo) {
        List<Pedido> pedidos = obtenerPedidosExitososDePlataforma(anio);

        Map<String, Double> agrupados = pedidos.stream()
                .collect(Collectors.groupingBy(
                        p -> etiqueta(p.getFecha(), intervalo),
                        Collectors.summingDouble(Pedido::getTotal)
                ));

        List<PuntoDatos> puntos = agrupados.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new PuntoDatos(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new SerieGraficaVentas(intervalo, puntos);
    }

    private String etiqueta(LocalDateTime fecha, IntervaloTiempo intervalo) {
        return switch (intervalo) {
            case DIARIO -> fecha.toLocalDate().toString();
            case SEMANAL -> fecha.getYear() + "-W" + String.format("%02d",
                    fecha.toLocalDate().get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear()));
            case MENSUAL -> fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue());
        };
    }
}
