package com.openlib.market.infrastructure.finanzas;

import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.BilleteraEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ItemPedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionBilleteraEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.BilleteraRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.TransaccionBilleteraRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class ActualizarBilleteraVentaEventListener {

    private final PedidoRepository pedidoRepository;
    private final ContenidoDigitalRepository contenidoDigitalRepository;
    private final BilleteraRepository billeteraRepository;
    private final TransaccionBilleteraRepository transaccionBilleteraRepository;

    private static final double COMISION_PLATAFORMA = 0.15; // 15%

    public ActualizarBilleteraVentaEventListener(PedidoRepository pedidoRepository,
                                                 ContenidoDigitalRepository contenidoDigitalRepository,
                                                 BilleteraRepository billeteraRepository,
                                                 TransaccionBilleteraRepository transaccionBilleteraRepository) {
        this.pedidoRepository = pedidoRepository;
        this.contenidoDigitalRepository = contenidoDigitalRepository;
        this.billeteraRepository = billeteraRepository;
        this.transaccionBilleteraRepository = transaccionBilleteraRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handlePedidoCompletado(PedidoCompletadoEvent event) {
        PedidoEntity pedido = pedidoRepository.findById(event.getIdPedido()).orElse(null);
        if (pedido == null) return;

        LocalDateTime now = LocalDateTime.now();

        for (ItemPedidoEntity item : pedido.getItems()) {
            ContenidoDigitalEntity contenido = contenidoDigitalRepository.findById(item.getIsbn()).orElse(null);
            
            if (contenido != null && contenido.getIdVendedor() != null) {
                String idVendedor = contenido.getIdVendedor();
                
                // Ignorar compras hechas por el mismo vendedor (si existiera el caso) o compras sin vendedor asociado (OpenLib directo)
                // Para este MVP, asumimos que todos los contenidos tienen vendedor
                
                double montoBruto = item.getCantidad() * item.getPrecioUnitario();
                double montoComision = montoBruto * COMISION_PLATAFORMA;
                double gananciaNeta = montoBruto - montoComision;

                // 1. Guardar transacción de VENTA
                TransaccionBilleteraEntity txVenta = new TransaccionBilleteraEntity(
                        idVendedor, now, "SALE", "Venta de libro: " + contenido.getTitulo(), montoBruto
                );
                transaccionBilleteraRepository.save(txVenta);

                // 2. Guardar transacción de COMISIÓN (Negativa)
                TransaccionBilleteraEntity txComision = new TransaccionBilleteraEntity(
                        idVendedor, now, "COMMISSION", "Comisión OpenLib (15%)", -montoComision
                );
                transaccionBilleteraRepository.save(txComision);

                // 3. Actualizar Billetera
                BilleteraEntity billetera = billeteraRepository.findById(idVendedor)
                        .orElse(new BilleteraEntity(idVendedor, 0.0));
                
                billetera.setSaldoDisponible(billetera.getSaldoDisponible() + gananciaNeta);
                billeteraRepository.save(billetera);
            }
        }
    }
}
