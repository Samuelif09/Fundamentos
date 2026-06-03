package com.openlib.market.application.finanzas;
import com.openlib.market.domain.finanzas.ComisionFactory;
import com.openlib.market.domain.finanzas.IComisionStrategy;
import com.openlib.market.domain.finanzas.IVentasReadGateway;
import com.openlib.market.domain.finanzas.VentaPlanaDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
public class GenerarReporteIngresosInteractor {
    private final IVentasReadGateway ventasReadGateway;
    private final ComisionFactory comisionFactory;
    public GenerarReporteIngresosInteractor(IVentasReadGateway ventasReadGateway, ComisionFactory comisionFactory) {
        this.ventasReadGateway = ventasReadGateway;
        this.comisionFactory = comisionFactory;
    }
    public IngresosVendedorResponseDto ejecutar(String vendedorId, LocalDate fechaDesde, LocalDate fechaHasta) {
        List<VentaPlanaDto> ventas = ventasReadGateway.obtenerVentasPorVendedorYFechas(vendedorId, fechaDesde, fechaHasta);
        BigDecimal totalVentasBrutas = BigDecimal.ZERO;
        BigDecimal totalComisiones = BigDecimal.ZERO;
        for (VentaPlanaDto venta : ventas) {
            BigDecimal subtotalBruto = venta.getPrecioUnitario().multiply(new BigDecimal(venta.getCantidad()));
            totalVentasBrutas = totalVentasBrutas.add(subtotalBruto);
            IComisionStrategy strategy = comisionFactory.obtenerEstrategia(venta.getTipoProducto());
            BigDecimal comisionPorUnidad = strategy.calcularComision(venta.getPrecioUnitario());
            BigDecimal comisionSubtotal = comisionPorUnidad.multiply(new BigDecimal(venta.getCantidad()));
            totalComisiones = totalComisiones.add(comisionSubtotal);
        }
        BigDecimal ingresoNeto = totalVentasBrutas.subtract(totalComisiones).setScale(2, RoundingMode.HALF_UP);
        return new IngresosVendedorResponseDto(
                totalVentasBrutas.setScale(2, RoundingMode.HALF_UP),
                totalComisiones.setScale(2, RoundingMode.HALF_UP),
                ingresoNeto
        );
    }
}
