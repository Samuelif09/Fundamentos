package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.CredencialApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialApiRepository extends JpaRepository<CredencialApiEntity, String> {
    Optional<CredencialApiEntity> findByValorLlave(String valorLlave);
}
