package com.openlib.market.domain.reembolso;

import java.util.List;
import java.util.Optional;

public interface IReembolsoGateway {
    void guardar(SolicitudReembolso solicitud);
    void actualizar(SolicitudReembolso solicitud);
    Optional<SolicitudReembolso> obtenerPorId(String id);
    List<SolicitudReembolso> listarTodas();
}
