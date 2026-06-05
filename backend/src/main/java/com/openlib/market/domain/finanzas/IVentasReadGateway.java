package com.openlib.market.domain.finanzas;
import java.time.LocalDate;
import java.util.List;
public interface IVentasReadGateway {
    List<VentaPlanaDto> obtenerVentasPorVendedorYFechas(String vendedorId, LocalDate fechaDesde, LocalDate fechaHasta);
    List<VentaPlanaDto> obtenerVentasGlobales(LocalDate fechaDesde, LocalDate fechaHasta);
}
