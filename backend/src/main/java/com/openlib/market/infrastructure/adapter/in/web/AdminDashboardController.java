package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.dashboardGlobal.IVerDashboardMetricasUseCase;
import com.openlib.market.application.dashboardGlobal.SerieGraficaDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminChartDataDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminKpiDto;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {

    private final IVerDashboardMetricasUseCase verDashboardMetricasUseCase;
    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final ContenidoDigitalRepository contenidoDigitalRepository;
    private final PedidoRepository pedidoRepository;

    public AdminDashboardController(IVerDashboardMetricasUseCase verDashboardMetricasUseCase,
                                    UsuarioRepository usuarioRepository,
                                    VendedorRepository vendedorRepository,
                                    ContenidoDigitalRepository contenidoDigitalRepository,
                                    PedidoRepository pedidoRepository) {
        this.verDashboardMetricasUseCase = verDashboardMetricasUseCase;
        this.usuarioRepository = usuarioRepository;
        this.vendedorRepository = vendedorRepository;
        this.contenidoDigitalRepository = contenidoDigitalRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/kpis")
    public ResponseEntity<AdminKpiDto> getKpis() {
        int totalUsers = (int) usuarioRepository.count();
        int pendingSellers = (int) vendedorRepository.countByEstadoVerificacion("EN_REVISION");
        int totalBooks = (int) contenidoDigitalRepository.countByEstado("PUBLICADO");
        
        double totalSales = pedidoRepository.findAll().stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstado()))
                .mapToDouble(PedidoEntity::getTotal)
                .sum();
        double platformRevenue = totalSales * 0.15; // 15% Comisión de la plataforma
        
        AdminKpiDto kpis = new AdminKpiDto(totalUsers, pendingSellers, totalBooks, platformRevenue);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/graficas")
    public ResponseEntity<AdminChartDataDto> getGraficas() {
        // 1. Ingresos por comisiones (15% de ventas brutas mensuales) para 2026
        SerieGraficaDto ingresosSerie = verDashboardMetricasUseCase.generarGraficaVentas("MENSUAL", 2026);
        
        List<AdminChartDataDto.DataPoint> revenueGrowth = new ArrayList<>();
        if (ingresosSerie != null && ingresosSerie.getPuntos() != null && !ingresosSerie.getPuntos().isEmpty()) {
            for (Map<String, Object> punto : ingresosSerie.getPuntos()) {
                String etiqueta = (String) punto.get("etiqueta");
                double valor = (Double) punto.get("valor");
                revenueGrowth.add(new AdminChartDataDto.DataPoint(etiqueta, valor * 0.15));
            }
        } else {
            // Si no hay datos, colocar 0 para cada mes del año
            String[] meses = {"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"};
            for (String mes : meses) {
                revenueGrowth.add(new AdminChartDataDto.DataPoint(mes, 0.0));
            }
        }

        // 2. Crecimiento de usuarios mensuales reales para 2026
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        Map<Integer, Long> usersByMonth = usuarios.stream()
                .filter(u -> u.getFechaRegistro() != null && u.getFechaRegistro().getYear() == 2026)
                .collect(Collectors.groupingBy(
                        u -> u.getFechaRegistro().getMonthValue(),
                        Collectors.counting()
                ));

        List<AdminChartDataDto.DataPoint> userGrowth = new ArrayList<>();
        String[] mesesNombres = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        for (int i = 1; i <= 12; i++) {
            long count = usersByMonth.getOrDefault(i, 0L);
            userGrowth.add(new AdminChartDataDto.DataPoint(mesesNombres[i - 1], (double) count));
        }

        AdminChartDataDto chartData = new AdminChartDataDto(userGrowth, revenueGrowth);
        return ResponseEntity.ok(chartData);
    }
}
