package com.openlib.market.domain.comunicado;

import java.util.List;

public interface INotificacionGateway {
    void enviarComunicadoMasivo(ComunicadoMasivo comunicado, List<String> correosDestinatarios);
}
