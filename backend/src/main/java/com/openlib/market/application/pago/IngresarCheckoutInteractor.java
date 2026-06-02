package com.openlib.market.application.pago;

import com.openlib.market.domain.pago.*;

import java.util.UUID;

public class IngresarCheckoutInteractor implements IIngresarCheckoutUseCase {

    private final IPasarelaPagoGateway pasarelaPago;
    private final IEventPublisher eventPublisher;
    private final com.openlib.market.domain.carrito.ICarritoGateway carritoGateway;
    private final IPedidoGateway pedidoGateway;

    public IngresarCheckoutInteractor(
            IPasarelaPagoGateway pasarelaPago, 
            IEventPublisher eventPublisher,
            com.openlib.market.domain.carrito.ICarritoGateway carritoGateway,
            IPedidoGateway pedidoGateway) {
        this.pasarelaPago = pasarelaPago;
        this.eventPublisher = eventPublisher;
        this.carritoGateway = carritoGateway;
        this.pedidoGateway = pedidoGateway;
    }

    @Override
    public void procesarCheckout(CheckoutRequestDto request) {
        // 1. Obtener carrito del usuario
        com.openlib.market.domain.carrito.IdUsuario idUsuarioCarrito = new com.openlib.market.domain.carrito.IdUsuario(request.getIdUsuario());
        com.openlib.market.domain.carrito.CarritoCompras carrito = carritoGateway.obtenerPorUsuario(idUsuarioCarrito)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un carrito activo."));

        if (carrito.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito est\u00e1 vac\u00edo.");
        }

        double montoCalculado = carrito.getTotal();
        Monto monto = new Monto(montoCalculado);
        TokenPago token = new TokenPago(request.getTokenPago());

        // 2. Procesar cobro con pasarela
        TransaccionPago transaccion = new TransaccionPago(UUID.randomUUID().toString(), token, monto);
        boolean cobroExitoso = pasarelaPago.procesarCobro(token, monto);

        if (!cobroExitoso) {
            transaccion.rechazar();
            throw new PagoRechazadoException("La pasarela de pago rechaz\u00f3 la transacci\u00f3n.");
        }
        transaccion.aprobar();

        // 3. Crear el Pedido y guardar
        Pedido pedido = new Pedido(
                request.getIdPedido(),
                UUID.randomUUID().toString(),
                request.getIdUsuario(),
                montoCalculado,
                EstadoPedido.PAGADO,
                java.time.LocalDateTime.now(),
                TipoMetodoPago.TARJETA
        );

        java.util.List<String> isbns = new java.util.ArrayList<>();
        for (com.openlib.market.domain.carrito.ItemCarrito item : carrito.getItems()) {
            isbns.add(item.getLibroIsbn());
            ItemPedido itemPedido = new ItemPedido(item.getLibroIsbn(), item.getCantidad().getValor(), item.getPrecioUnitario());
            pedido.addItem(itemPedido);
        }

        pedidoGateway.guardar(pedido);

        // 4. Vaciar el carrito
        carrito.vaciar();
        carritoGateway.guardar(carrito);

        // 5. Publicar evento
        PedidoCompletadoEvent evento = new PedidoCompletadoEvent(
                request.getIdPedido(),
                request.getIdUsuario(),
                montoCalculado,
                isbns
        );
        eventPublisher.publicar(evento);
    }
}
