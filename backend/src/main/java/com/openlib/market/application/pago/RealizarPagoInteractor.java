package com.openlib.market.application.pago;

import com.openlib.market.domain.pago.*;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.domain.carrito.CarritoCompras;

public class RealizarPagoInteractor implements IRealizarPagoUseCase {

    private final IPedidoGateway pedidoGateway;
    private final IPasarelaPagoFactory pasarelaFactory;
    private final ICarritoGateway carritoGateway; // Dependencia de otro bounded context para Facade

    public RealizarPagoInteractor(IPedidoGateway pedidoGateway, IPasarelaPagoFactory pasarelaFactory, ICarritoGateway carritoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.pasarelaFactory = pasarelaFactory;
        this.carritoGateway = carritoGateway;
    }

    @Override
    public void realizarPago(RealizarPagoRequestDto request) {
        MetodoPago metodoPago = new MetodoPago(request.getTipoPago(), request.getDetallePago());
        
        // 1. Generar el pedido
        Pedido pedido = new Pedido(request.getSesionId(), request.getMontoTotal(), metodoPago.getTipo());
        pedidoGateway.guardar(pedido); // Se guarda PENDIENTE
        
        // 2. Procesar pago (Abstract Factory)
        IPagoExternoGateway pasarela;
        try {
            pasarela = pasarelaFactory.obtenerPasarela(metodoPago.getTipo());
        } catch (IllegalArgumentException e) {
            pedido.marcarComoFallido();
            pedidoGateway.guardar(pedido);
            throw new IllegalArgumentException("Método de pago no soportado: " + metodoPago.getTipo());
        }

        boolean aprobado = pasarela.procesar(pedido.getTotal(), metodoPago);

        if (aprobado) {
            // 3. Marcar como pagado y vaciar carrito (Facade)
            pedido.marcarComoPagado();
            pedidoGateway.guardar(pedido);
            
            // Vaciar carrito
            carritoGateway.obtenerPorSesionId(new SesionId(request.getSesionId())).ifPresent(carrito -> {
                // Al reemplazarlo por uno nuevo, queda vacío
                carritoGateway.guardar(new CarritoCompras(new SesionId(request.getSesionId())));
            });
            
        } else {
            pedido.marcarComoFallido();
            pedidoGateway.guardar(pedido);
            throw new PagoRechazadoException("El pago ha sido rechazado por la pasarela.");
        }
    }
}
