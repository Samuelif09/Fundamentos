package com.openlib.market.application.configuracion;

import java.util.List;

public interface IGestionarConfiguracionSistemaUseCase {
    void cambiarEstadoMetodoPago(String id, String estado);
    List<MetodoPagoConfigDto> listarMetodosPago();
}
