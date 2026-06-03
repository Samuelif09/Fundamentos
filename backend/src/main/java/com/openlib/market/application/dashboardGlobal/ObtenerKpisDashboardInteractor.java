package com.openlib.market.application.dashboardGlobal;

import com.openlib.market.application.finanzas.GenerarRentabilidadPlataformaInteractor;
import com.openlib.market.application.finanzas.RentabilidadPlataformaResponseDto;
import com.openlib.market.domain.dashboardGlobal.IKpiReadGateway;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminKpiDto;

import java.time.LocalDate;

public class ObtenerKpisDashboardInteractor {

    private final IKpiReadGateway kpiReadGateway;
    private final GenerarRentabilidadPlataformaInteractor rentabilidadPlataformaInteractor;

    public ObtenerKpisDashboardInteractor(IKpiReadGateway kpiReadGateway, GenerarRentabilidadPlataformaInteractor rentabilidadPlataformaInteractor) {
        this.kpiReadGateway = kpiReadGateway;
        this.rentabilidadPlataformaInteractor = rentabilidadPlataformaInteractor;
    }

    public AdminKpiDto ejecutar() {
        long totalUsuarios = kpiReadGateway.contarUsuariosTotales();
        long vendedoresPendientes = kpiReadGateway.contarVendedoresPendientes();
        long contenidosActivos = kpiReadGateway.contarContenidosActivos();

        // Para el KPI global de rentabilidad consideramos un periodo muy amplio (histórico completo)
        LocalDate desde = LocalDate.of(2000, 1, 1);
        LocalDate hasta = LocalDate.now().plusDays(1);
        
        RentabilidadPlataformaResponseDto rentabilidad = rentabilidadPlataformaInteractor.ejecutar(desde, hasta);
        double platformRevenue = rentabilidad.getTotalComisionesPlataforma().doubleValue();

        return new AdminKpiDto(
                (int) totalUsuarios,
                (int) vendedoresPendientes,
                (int) contenidosActivos,
                platformRevenue
        );
    }
}
