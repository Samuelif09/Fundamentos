package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.AgrupacionTiempo;
import com.openlib.market.domain.finanzas.ComisionFactory;
import com.openlib.market.domain.finanzas.IComisionStrategy;
import com.openlib.market.domain.finanzas.IVentasReadGateway;
import com.openlib.market.domain.finanzas.VentaPlanaDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerarMetricasVentasVendedorInteractor {

    private final IVentasReadGateway ventasReadGateway;
    private final ComisionFactory comisionFactory;

    public GenerarMetricasVentasVendedorInteractor(IVentasReadGateway ventasReadGateway, ComisionFactory comisionFactory) {
        this.ventasReadGateway = ventasReadGateway;
        this.comisionFactory = comisionFactory;
    }

    public List<MetricaTemporalResponseDto> ejecutar(String vendedorId, LocalDate desde, LocalDate hasta, AgrupacionTiempo agrupacion) {
        List<VentaPlanaDto> ventas = ventasReadGateway.obtenerVentasPorVendedorYFechas(vendedorId, desde, hasta);

        Map<String, List<VentaPlanaDto>> agrupadas = ventas.stream()
                .collect(Collectors.groupingBy(venta -> formatearPeriodo(venta.getFecha().toLocalDate(), agrupacion)));

        return agrupadas.entrySet().stream()
                .map(entry -> {
                    String periodo = entry.getKey();
                    List<VentaPlanaDto> ventasPeriodo = entry.getValue();

                    int cantidadTotal = ventasPeriodo.stream()
                            .mapToInt(VentaPlanaDto::getCantidad)
                            .sum();

                    BigDecimal ingresoNetoPeriodo = ventasPeriodo.stream()
                            .map(venta -> {
                                BigDecimal bruto = venta.getPrecioUnitario().multiply(new BigDecimal(venta.getCantidad()));
                                IComisionStrategy strategy = comisionFactory.obtenerEstrategia(venta.getTipoProducto());
                                BigDecimal comisionPorUnidad = strategy.calcularComision(venta.getPrecioUnitario());
                                BigDecimal comisionSubtotal = comisionPorUnidad.multiply(new BigDecimal(venta.getCantidad()));
                                return bruto.subtract(comisionSubtotal);
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP);

                    return new MetricaTemporalResponseDto(periodo, cantidadTotal, ingresoNetoPeriodo);
                })
                .sorted(Comparator.comparing(MetricaTemporalResponseDto::getPeriodo))
                .collect(Collectors.toList());
    }

    private String formatearPeriodo(LocalDate fecha, AgrupacionTiempo agrupacion) {
        switch (agrupacion) {
            case DIARIA:
                return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            case SEMANAL:
                int week = fecha.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                int year = fecha.get(IsoFields.WEEK_BASED_YEAR);
                return String.format("%d-W%02d", year, week);
            case MENSUAL:
                return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            default:
                return fecha.toString();
        }
    }
}
