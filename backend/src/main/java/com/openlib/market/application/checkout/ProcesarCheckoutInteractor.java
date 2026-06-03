package com.openlib.market.application.checkout;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.domain.checkout.*;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.shared.ReglaNegocioInvalidaException;

public class ProcesarCheckoutInteractor {

    private final ICarritoGateway carritoGateway;
    private final IPedidoGateway pedidoGateway;
    private final IPasarelaPagoSimuladaGateway pasarelaPagoGateway;
    private final ICheckoutEventPublisher eventPublisher;
    private final PedidoFactory pedidoFactory;

    public ProcesarCheckoutInteractor(ICarritoGateway carritoGateway, IPedidoGateway pedidoGateway, IPasarelaPagoSimuladaGateway pasarelaPagoGateway, ICheckoutEventPublisher eventPublisher, PedidoFactory pedidoFactory) {
        this.carritoGateway = carritoGateway;
        this.pedidoGateway = pedidoGateway;
        this.pasarelaPagoGateway = pasarelaPagoGateway;
        this.eventPublisher = eventPublisher;
        this.pedidoFactory = pedidoFactory;
    }

    public void ejecutar(String sesionIdStr, String idUsuario, String metodoPagoStr) {
        SesionId sesionId = new SesionId(sesionIdStr);
        CarritoCompras carrito = carritoGateway.obtenerPorSesionId(sesionId)
                .orElseThrow(() -> new ReglaNegocioInvalidaException("El carrito no existe para la sesión indicada"));

        // FIX: Sumar los subtotales de los ítems de manera explícita (Mapeo de Carrito a Pedido)
        double sumatoriaSubtotales = 0.0;
        java.util.List<com.openlib.market.domain.pago.ItemPedido> itemsPedido = new java.util.ArrayList<>();
        
        for (com.openlib.market.domain.carrito.ItemCarrito itemCarrito : carrito.getItems()) {
            com.openlib.market.domain.pago.ItemPedido itemPedido = new com.openlib.market.domain.pago.ItemPedido(
                    itemCarrito.getLibroIsbn(),
                    itemCarrito.getCantidad().getValor(),
                    itemCarrito.getPrecioUnitario()
            );
            itemsPedido.add(itemPedido);
            sumatoriaSubtotales += (itemCarrito.getCantidad().getValor() * itemCarrito.getPrecioUnitario());
        }

        // Calcular total con Decorators
        CalculadorPrecio calculadorBase = new PrecioBase(sumatoriaSubtotales);
        CalculadorPrecio calculadorConImpuesto = new ImpuestoDecorator(calculadorBase, 0.19); // 19% IVA
        double totalCalculado = calculadorConImpuesto.calcularTotal();

        // Procesar pago
        String transaccionId = pasarelaPagoGateway.procesarPago(totalCalculado);

        if (transaccionId == null || transaccionId.isEmpty()) {
            throw new ReglaNegocioInvalidaException("El pago fue rechazado o falló");
        }

        // Mapear el metodo de pago de string a Enum
        com.openlib.market.domain.pago.TipoMetodoPago metodoPago;
        try {
            metodoPago = com.openlib.market.domain.pago.TipoMetodoPago.valueOf(metodoPagoStr.toUpperCase());
        } catch (Exception e) {
            throw new ReglaNegocioInvalidaException("Método de pago inválido");
        }

        // Crear pedido mediante factory
        Pedido pedido = pedidoFactory.crearDesdeCarrito(carrito, sesionIdStr, totalCalculado, idUsuario, metodoPago);

        // FIX: Transferencia lógica y explícita de los agregados (ítems) al Pedido recién creado
        for (com.openlib.market.domain.pago.ItemPedido item : itemsPedido) {
            pedido.addItem(item);
        }

        // Cambiar estado del pedido usando el patrón State
        pedido.marcarComoPagado();

        // Guardar pedido (ya hidratado con todos los Items)
        pedidoGateway.guardar(pedido);

        // Emitir Evento (Observer)
        eventPublisher.publicar(new CheckoutCompletadoEvent(pedido.getId(), sesionIdStr));
    }
}
