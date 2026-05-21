package com.openlib.market.application.estadisticas;

import com.openlib.market.domain.estadisticas.EstadisticaLector;

public interface IVerEstadisticasMiCuentaUseCase {
    EstadisticaLector obtenerEstadisticas(String idUsuario);
}
