package com.openlib.market.application.anomalias;

import com.openlib.market.domain.anomalias.Alerta;

public interface INotificacionGateway {
    void enviarAlerta(Alerta alerta);
}
