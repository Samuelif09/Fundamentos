package com.openlib.market.domain.curaduria;

public interface IInteligenciaArtificialGateway {
    ScoreToxicidad analizarTexto(String texto);
}
