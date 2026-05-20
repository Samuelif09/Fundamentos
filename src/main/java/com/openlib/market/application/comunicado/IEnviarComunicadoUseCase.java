package com.openlib.market.application.comunicado;

public interface IEnviarComunicadoUseCase {
    ComunicadoDto enviar(String asunto, String cuerpoMensaje, String filtroStr);
}
