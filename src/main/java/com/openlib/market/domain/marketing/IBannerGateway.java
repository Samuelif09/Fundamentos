package com.openlib.market.domain.marketing;

import java.util.List;

public interface IBannerGateway {
    void guardar(BannerPromocional banner);
    void actualizar(BannerPromocional banner);
    BannerPromocional obtenerPorId(String id);
    List<BannerPromocional> listarTodos();
}
