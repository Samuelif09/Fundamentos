package com.openlib.market.application.transaccionesAdmin;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VerTransaccionesAdminInteractor implements IVerTransaccionesAdminUseCase {

    private final IPedidoGateway pedidoGateway;

    public VerTransaccionesAdminInteractor(IPedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    @Override
    public List<TransaccionGlobalDto> listarTransacciones(int page, int size) {
        List<Pedido> pedidos = pedidoGateway.listarTodos(page, size);

        return pedidos.stream().map(p -> new TransaccionGlobalDto(
                p.getId(),
                p.getIdUsuario(),
                p.getTotal(),
                p.getEstado().name(),
                p.getFecha() != null ? p.getFecha().toString() : "N/A",
                p.getTipoMetodoPago() != null ? p.getTipoMetodoPago().name() : "N/A"
        )).collect(Collectors.toList());
    }
}
