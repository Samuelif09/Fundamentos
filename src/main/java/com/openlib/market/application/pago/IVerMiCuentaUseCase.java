package com.openlib.market.application.pago;

import java.util.List;

public interface IVerMiCuentaUseCase {
    List<HistorialPedidoResponseDto> obtenerHistorial(String idUsuario, int offset, int limit);
}
