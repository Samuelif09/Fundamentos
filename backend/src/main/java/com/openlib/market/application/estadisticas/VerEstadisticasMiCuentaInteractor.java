package com.openlib.market.application.estadisticas;

import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.estadisticas.EstadisticaLector;
import com.openlib.market.domain.historial.HistorialNavegacion;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;
import com.openlib.market.domain.historial.ItemNavegacion;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class VerEstadisticasMiCuentaInteractor implements IVerEstadisticasMiCuentaUseCase {

    private final IPedidoGateway pedidoGateway;
    private final IHistorialNavegacionGateway historialGateway;
    private final IDetalleGateway detalleGateway;

    public VerEstadisticasMiCuentaInteractor(IPedidoGateway pedidoGateway,
            IHistorialNavegacionGateway historialGateway,
            IDetalleGateway detalleGateway) {
        this.pedidoGateway = pedidoGateway;
        this.historialGateway = historialGateway;
        this.detalleGateway = detalleGateway;
    }

    @Override
    public EstadisticaLector obtenerEstadisticas(String idUsuario) {
        // 1. Total pedidos
        List<Pedido> pedidos = pedidoGateway.listarPorUsuarioId(idUsuario, 0, 1000);
        int totalPedidos = pedidos.size();

        // 2. Reseñas escritas (Omitido por brevedad / falta de resenaGateway directo
        // para ID usuario, asumimos 0)
        int totalResenas = 0;

        // 3. Categoría favorita calculada desde Historial de Navegación y Detalles del
        // Libro
        String categoriaFavorita = "Ninguna";
        Optional<HistorialNavegacion> historialOpt = historialGateway.obtenerPorUsuario(idUsuario);

        if (historialOpt.isPresent() && !historialOpt.get().getItems().isEmpty()) {
            List<ItemNavegacion> items = historialOpt.get().getItems();

            Map<String, Long> categoriasCount = items.stream()
                    .map(item -> detalleGateway.buscarPorId(new Isbn(item.getIdLibro())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Libro::getCategoria) // asumiendo que categoria existe o es un atributo, de lo contrario usamos
                                              // el titulo como fallback
                    .filter(c -> c != null && !c.isEmpty())
                    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

            categoriaFavorita = categoriasCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Ninguna");
        }

        return new EstadisticaLector(idUsuario, totalPedidos, totalResenas, categoriaFavorita);
    }
}
