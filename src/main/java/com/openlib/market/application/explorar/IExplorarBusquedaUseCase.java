package com.openlib.market.application.explorar;

import com.openlib.market.domain.explorar.CriterioTendencia;
import java.util.List;

public interface IExplorarBusquedaUseCase {
    List<LibroTendenciaDto> explorarTendencias(CriterioTendencia criterio);
}
