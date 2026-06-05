package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.finanzas.IVentasReadGateway;
import com.openlib.market.domain.finanzas.VentaPlanaDto;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VentasJpaReadGateway implements IVentasReadGateway {
    
    private final PedidoRepository pedidoRepository;
    private final ContenidoDigitalRepository contenidoRepository;

    public VentasJpaReadGateway(PedidoRepository pedidoRepository, ContenidoDigitalRepository contenidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.contenidoRepository = contenidoRepository;
    }

    @Override
    public List<VentaPlanaDto> obtenerVentasPorVendedorYFechas(String vendedorId, LocalDate fechaDesde, LocalDate fechaHasta) {
        List<VentaPlanaDto> result = new ArrayList<>();
        List<PedidoEntity> pedidos = pedidoRepository.findAll();
        
        for (PedidoEntity p : pedidos) {
            if ("PAGADO".equalsIgnoreCase(p.getEstado()) && 
                p.getFecha().compareTo(fechaDesde.atStartOfDay()) >= 0 &&
                p.getFecha().compareTo(fechaHasta.plusDays(1).atStartOfDay()) < 0) {
                
                for (var item : p.getItems()) {
                    contenidoRepository.findById(item.getIsbn()).ifPresent(c -> {
                        if (vendedorId.equals(c.getIdVendedor())) {
                            String typeStr = c.getClass().getSimpleName().replace("Entity", "").toUpperCase();
                            result.add(new VentaPlanaDto(typeStr, BigDecimal.valueOf(item.getPrecioUnitario()), item.getCantidad(), p.getFecha()));
                        }
                    });
                }
            }
        }
        return result;
    }

    @Override
    public List<VentaPlanaDto> obtenerVentasGlobales(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<VentaPlanaDto> result = new ArrayList<>();
        List<PedidoEntity> pedidos = pedidoRepository.findAll();
        
        for (PedidoEntity p : pedidos) {
            if ("PAGADO".equalsIgnoreCase(p.getEstado()) && 
                p.getFecha().compareTo(fechaDesde.atStartOfDay()) >= 0 &&
                p.getFecha().compareTo(fechaHasta.plusDays(1).atStartOfDay()) < 0) {
                
                for (var item : p.getItems()) {
                    contenidoRepository.findById(item.getIsbn()).ifPresent(c -> {
                        String typeStr = c.getClass().getSimpleName().replace("Entity", "").toUpperCase();
                        result.add(new VentaPlanaDto(typeStr, BigDecimal.valueOf(item.getPrecioUnitario()), item.getCantidad(), p.getFecha()));
                    });
                }
            }
        }
        return result;
    }
}
