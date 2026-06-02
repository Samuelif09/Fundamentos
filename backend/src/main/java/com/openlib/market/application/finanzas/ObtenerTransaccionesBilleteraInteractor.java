package com.openlib.market.application.finanzas;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionBilleteraEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.TransaccionBilleteraRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObtenerTransaccionesBilleteraInteractor {

    private final TransaccionBilleteraRepository repository;

    public ObtenerTransaccionesBilleteraInteractor(TransaccionBilleteraRepository repository) {
        this.repository = repository;
    }

    public List<TransactionDto> obtenerTransacciones(String idVendedor) {
        List<TransaccionBilleteraEntity> entities = repository.findByIdVendedorOrderByFechaDesc(idVendedor);

        return entities.stream().map(e -> new TransactionDto(
                e.getId(),
                e.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                e.getTipo(),
                e.getDescripcion(),
                e.getMonto()
        )).collect(Collectors.toList());
    }
}
