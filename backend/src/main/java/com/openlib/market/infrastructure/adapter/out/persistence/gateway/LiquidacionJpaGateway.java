package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionFinancieraEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.TransaccionFinancieraRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class LiquidacionJpaGateway implements ILiquidacionGateway {

    private final TransaccionFinancieraRepository repository;

    public LiquidacionJpaGateway(TransaccionFinancieraRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TransaccionFinanciera> obtenerTransaccionesPorVendedor(String idVendedor) {
        return repository.findByIdVendedor(idVendedor).stream()
                .map(e -> new TransaccionFinanciera(e.getIdTransaccion(), e.getSubtotal(), e.getFecha()))
                .collect(Collectors.toList());
    }

    public List<TransaccionFinanciera> obtenerTransaccionesPorPeriodo(String idVendedor, LocalDate desde, LocalDate hasta) {
        return repository.findByVendedorYPeriodo(idVendedor, desde, hasta).stream()
                .map(e -> new TransaccionFinanciera(e.getIdTransaccion(), e.getSubtotal(), e.getFecha()))
                .collect(Collectors.toList());
    }

    public double sumarIngresosPorPeriodo(String idVendedor, LocalDate desde, LocalDate hasta) {
        Double suma = repository.sumarIngresosPorPeriodo(idVendedor, desde, hasta);
        return suma != null ? suma : 0.0;
    }

    public void guardar(String idVendedor, double subtotal, LocalDate fecha) {
        repository.save(new TransaccionFinancieraEntity(
                java.util.UUID.randomUUID().toString(), idVendedor, subtotal, fecha));
    }
}
