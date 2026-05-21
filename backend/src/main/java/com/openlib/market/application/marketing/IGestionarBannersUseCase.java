package com.openlib.market.application.marketing;

import java.util.List;

public interface IGestionarBannersUseCase {
    BannerDto crearBanner(String titulo, String urlImagen, String urlDestino, String fechaInicio, String fechaFin);
    void cambiarEstado(String id, String estado);
    List<BannerDto> listarBanners();
}
