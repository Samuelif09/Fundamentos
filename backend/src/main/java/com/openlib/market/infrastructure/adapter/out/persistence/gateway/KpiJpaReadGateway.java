package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.dashboardGlobal.IKpiReadGateway;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class KpiJpaReadGateway implements IKpiReadGateway {

    private final EntityManager entityManager;

    public KpiJpaReadGateway(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long contarUsuariosTotales() {
        String hql = "SELECT COUNT(u) FROM UsuarioEntity u";
        return (long) entityManager.createQuery(hql).getSingleResult();
    }

    @Override
    public long contarVendedoresPendientes() {
        String hql = "SELECT COUNT(v) FROM VendedorEntity v WHERE v.estadoVerificacion = 'PENDIENTE'";
        return (long) entityManager.createQuery(hql).getSingleResult();
    }

    @Override
    public long contarContenidosActivos() {
        String hql = "SELECT COUNT(c) FROM ContenidoDigitalEntity c WHERE c.estado = 'PUBLICADO' OR c.estado = 'ACTIVO'";
        return (long) entityManager.createQuery(hql).getSingleResult();
    }
}
