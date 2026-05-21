package com.openlib.market.application.historial;

import java.util.List;

public interface IVerHistorialNavegacionUseCase {
    List<ItemHistorialResponseDto> verHistorial(String idUsuario);
}
