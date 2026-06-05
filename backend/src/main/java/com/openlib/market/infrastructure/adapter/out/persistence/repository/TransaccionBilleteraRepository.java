package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionBilleteraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionBilleteraRepository extends JpaRepository<TransaccionBilleteraEntity, String> {
    List<TransaccionBilleteraEntity> findByIdVendedorOrderByFechaDesc(String idVendedor);
}
