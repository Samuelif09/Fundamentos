package com.openlib.market.application.dashboardVendedor;

import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VentaVendedorProjection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObtenerFinanzasVendedorInteractor {

    private final PedidoRepository pedidoRepository;

    public ObtenerFinanzasVendedorInteractor(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public VendedorFinanzasDto obtenerFinanzas(String idVendedor) {
        List<VentaVendedorProjection> ventas = pedidoRepository.findVentasPorVendedor(idVendedor);

        double totalRevenue = 0;
        int totalOrders = 0;
        java.util.Set<String> pedidosUnicos = new java.util.HashSet<>();

        for (VentaVendedorProjection venta : ventas) {
            totalRevenue += venta.getCantidad() * venta.getPrecioUnitario();
            pedidosUnicos.add(venta.getPedidoId());
        }

        totalOrders = pedidosUnicos.size();
        
        // El saldo pendiente por ahora es igual a los ingresos totales
        double pendingBalance = totalRevenue;

        return new VendedorFinanzasDto(totalRevenue, pendingBalance, totalOrders);
    }
}
