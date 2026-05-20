package com.openlib.market.application.soporte;

public interface IGestionarSoporteUseCase {
    DisputaDto iniciarMediacion(String disputaId);
    DisputaDto resolverDisputa(String disputaId, String resolucionStr);
}
