package com.openlib.market.infrastructure.chatbot;

import com.openlib.market.domain.chatbot.IChatbotGateway;
import com.openlib.market.domain.chatbot.RespuestaBot;
import org.springframework.stereotype.Component;

@Component
public class ChatbotDummyGateway implements IChatbotGateway {

    @Override
    public RespuestaBot consultar(String mensaje) {
        String msgLower = mensaje.toLowerCase();

        if (msgLower.contains("reembolso")) {
            return new RespuestaBot("Para solicitar un reembolso, ve a Mis Pedidos -> Seleccionar Pedido -> Reembolso.", 0.9);
        }
        if (msgLower.contains("contraseña") || msgLower.contains("password")) {
            return new RespuestaBot("Puedes restablecer tu contraseña en la página de Login haciendo click en 'Olvidé mi contraseña'.", 0.95);
        }
        if (msgLower.contains("envío") || msgLower.contains("llegar")) {
            return new RespuestaBot("Los envíos estándar tardan entre 3 y 5 días hábiles.", 0.85);
        }

        // Si no reconoce el patrón, devuelve confianza baja
        return new RespuestaBot("No estoy seguro de entender tu pregunta. Déjame buscar información...", 0.4);
    }
}
