package com.openlib.market.application.ventavendedor;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import com.openlib.market.domain.ventavendedor.IDetalleLibroGateway;
import com.openlib.market.domain.ventavendedor.INotificacionVendedorGateway;
import com.openlib.market.domain.ventavendedor.NotificacionVendedor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecibirVentasInteractor implements IRecibirVentasUseCase {

    private final IDetalleLibroGateway detalleLibroGateway;
    private final INotificacionVendedorGateway notificacionGateway;

    public RecibirVentasInteractor(IDetalleLibroGateway detalleLibroGateway, INotificacionVendedorGateway notificacionGateway) {
        this.detalleLibroGateway = detalleLibroGateway;
        this.notificacionGateway = notificacionGateway;
    }

    @Override
    public void onPedidoCompletado(PedidoCompletadoEvent event) {
        if (event.getIsbns() == null || event.getIsbns().isEmpty()) {
            return;
        }

        // Agrupar ISBNs por Vendedor
        Map<String, List<String>> ventasPorVendedor = new HashMap<>();

        for (String isbn : event.getIsbns()) {
            Optional<String> vendedorOpt = detalleLibroGateway.obtenerIdVendedorPorIsbn(isbn);
            vendedorOpt.ifPresent(idVendedor -> {
                ventasPorVendedor.computeIfAbsent(idVendedor, k -> new ArrayList<>()).add(isbn);
            });
        }

        // Emitir notificaciones para cada vendedor afectado
        for (Map.Entry<String, List<String>> entry : ventasPorVendedor.entrySet()) {
            NotificacionVendedor notificacion = new NotificacionVendedor(
                    entry.getKey(),
                    event.getIdPedido(),
                    entry.getValue()
            );
            notificacionGateway.notificarVenta(notificacion);
        }
    }
}
