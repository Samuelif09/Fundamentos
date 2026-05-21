package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.finanzas.DatosFiscalesComprador;
import com.openlib.market.domain.finanzas.DatosFiscalesVendedor;
import com.openlib.market.domain.finanzas.DesgloseImpuestos;
import com.openlib.market.domain.finanzas.FacturaTributaria;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.FacturaEntity;
import org.springframework.stereotype.Component;

@Component
public class FacturacionMapper {

    public FacturaTributaria toDomain(FacturaEntity entity) {
        return new FacturaTributaria(
                entity.getIdFactura(),
                entity.getIdPedido(),
                entity.getFechaEmision(),
                new DatosFiscalesVendedor(
                        entity.getVendedorId(),
                        entity.getVendedorIdentificacion(),
                        entity.getVendedorRazonSocial()
                ),
                new DatosFiscalesComprador(
                        entity.getCompradorId(),
                        entity.getCompradorNombre(),
                        entity.getCompradorCorreo()
                ),
                new DesgloseImpuestos(entity.getImpuestoSubtotal()) // El desglose de impuestos calcula el IVA interno a partir del subtotal
        );
    }

    public FacturaEntity toEntity(FacturaTributaria domain) {
        FacturaEntity entity = new FacturaEntity();
        entity.setIdFactura(domain.getIdFactura());
        entity.setIdPedido(domain.getIdPedido());
        entity.setFechaEmision(domain.getFechaEmision());

        entity.setVendedorId(domain.getVendedor().getIdVendedor());
        entity.setVendedorIdentificacion(domain.getVendedor().getIdentificacionTributaria());
        entity.setVendedorRazonSocial(domain.getVendedor().getRazonSocial());

        entity.setCompradorId(domain.getComprador().getIdUsuario());
        entity.setCompradorNombre(domain.getComprador().getNombre());
        entity.setCompradorCorreo(domain.getComprador().getCorreo());

        entity.setImpuestoSubtotal(domain.getDesgloseImpuestos().getSubtotal());
        entity.setImpuestoIva(domain.getDesgloseImpuestos().getIva());
        entity.setImpuestoTotal(domain.getDesgloseImpuestos().getTotal());

        return entity;
    }
}
