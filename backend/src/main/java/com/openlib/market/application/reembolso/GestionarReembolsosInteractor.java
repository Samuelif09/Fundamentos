package com.openlib.market.application.reembolso;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.reembolso.IPasarelaPagoGateway;
import com.openlib.market.domain.reembolso.IReembolsoGateway;
import com.openlib.market.domain.reembolso.SolicitudReembolso;

import java.util.Optional;

@Service
public class GestionarReembolsosInteractor implements IGestionarReembolsosUseCase {

    private final IReembolsoGateway reembolsoGateway;
    private final IPedidoGateway pedidoGateway;
    private final IPasarelaPagoGateway pasarelaPagoGateway;

    public GestionarReembolsosInteractor(IReembolsoGateway reembolsoGateway, IPedidoGateway pedidoGateway, IPasarelaPagoGateway pasarelaPagoGateway) {
        this.reembolsoGateway = reembolsoGateway;
        this.pedidoGateway = pedidoGateway;
        this.pasarelaPagoGateway = pasarelaPagoGateway;
    }

    @Override
    public ReembolsoDto solicitarReembolso(String idPedido, double monto, String motivo) {
        Optional<Pedido> pedidoOpt = pedidoGateway.obtenerPorId(idPedido);
        Pedido pedido = pedidoOpt.orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        SolicitudReembolso solicitud = new SolicitudReembolso(idPedido, monto, motivo, pedido);
        reembolsoGateway.guardar(solicitud);

        return new ReembolsoDto(solicitud.getId(), solicitud.getIdPedido(), solicitud.getMontoReembolso(), solicitud.getMotivo(), solicitud.getEstado().name());
    }

    @Override
    public void aprobarReembolso(String idSolicitud) {
        SolicitudReembolso solicitud = reembolsoGateway.obtenerPorId(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        boolean operacionExitosa = pasarelaPagoGateway.ejecutarReembolso(solicitud.getIdPedido(), solicitud.getMontoReembolso());
        if (!operacionExitosa) {
            throw new RuntimeException("Error en la pasarela de pago al procesar el reembolso");
        }

        solicitud.aprobar();
        reembolsoGateway.actualizar(solicitud);
    }

    @Override
    public void denegarReembolso(String idSolicitud) {
        SolicitudReembolso solicitud = reembolsoGateway.obtenerPorId(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        solicitud.denegar();
        reembolsoGateway.actualizar(solicitud);
    }
}
