package com.openlib.market.infrastructure.chatbot;

import com.openlib.market.domain.chatbot.ITicketSoporteGateway;
import org.springframework.stereotype.Component;

@Component
public class TicketSoporteChatbotAdapter implements ITicketSoporteGateway {

    @Override
    public void crearTicket(String idUsuario, String mensajeContexto) {
        System.out.println("🎟️ [TICKET CREADO AUTOMÁTICAMENTE]");
        System.out.println("Usuario: " + idUsuario);
        System.out.println("Contexto: " + mensajeContexto);
        // En una implementación real, aquí se inyectaría ITicketSoporteGateway del dominio de soporte y se delegaría.
    }
}
