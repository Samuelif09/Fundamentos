package com.openlib.market.application.chatbot;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.chatbot.*;

@Service
public class ProcesarMensajeChatbotInteractor implements IProcesarMensajeChatbotUseCase {

    private final IChatbotGateway chatbotGateway;
    private final ITicketSoporteGateway ticketGateway;

    public ProcesarMensajeChatbotInteractor(IChatbotGateway chatbotGateway, ITicketSoporteGateway ticketGateway) {
        this.chatbotGateway = chatbotGateway;
        this.ticketGateway = ticketGateway;
    }

    @Override
    public String procesarMensaje(String idUsuario, String mensaje) {
        SesionChatbot sesion = new SesionChatbot(idUsuario, mensaje);
        
        RespuestaBot respuestaIa = chatbotGateway.consultar(mensaje);
        sesion.procesarRespuesta(respuestaIa);
        
        if (sesion.getEstado() == EstadoSesion.ESCALADA_A_HUMANO) {
            ticketGateway.crearTicket(idUsuario, mensaje);
        }
        
        return sesion.getRespuestaActualBot();
    }
}
