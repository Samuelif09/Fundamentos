package com.openlib.market.application.monitoreo;

import java.util.List;

public interface IEvaluarAnomaliaUseCase {
    void evaluarReglas(List<com.openlib.market.domain.monitoreo.ReglaAnomalia> reglas);
}
