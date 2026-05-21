package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.ReglaComisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReglaComisionRepository extends JpaRepository<ReglaComisionEntity, String> {
    Optional<ReglaComisionEntity> findByIdCategoria(String idCategoria);
}
