package com.openlib.market.application.pago;

import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;

import java.util.List;

public class VerMiCuentaInteractor implements IVerMiCuentaUseCase {

    private final IPedidoGateway pedidoGateway;

    public VerMiCuentaInteractor(IPedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    @Override
    public List<HistorialPedidoResponseDto> obtenerHistorial(String idUsuario, int offset, int limit) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }

        List<Pedido> pedidos = pedidoGateway.listarPorUsuarioId(idUsuario, offset, limit);

        return pedidos.stream()
                .map(p -> new HistorialPedidoResponseDto(
                        p.getId(),
                        p.getTotal(),
                        p.getEstado().name(),
                        p.getFecha()
                ))
                .toList();
    }
}
