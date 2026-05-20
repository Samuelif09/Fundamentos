package com.openlib.market.domain.chatbot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SesionChatbotTest {

    @Test
    void debeMantenerActivaSiConfianzaEsAlta() {
        SesionChatbot sesion = new SesionChatbot("user1", "Hola, quiero un reembolso");
        sesion.procesarRespuesta(new RespuestaBot("Claro, el proceso es el siguiente...", 0.9));
        
        assertEquals(EstadoSesion.ACTIVA, sesion.getEstado());
        assertEquals("Claro, el proceso es el siguiente...", sesion.getRespuestaActualBot());
    }

    @Test
    void debeEscalarSiConfianzaEsBaja() {
        SesionChatbot sesion = new SesionChatbot("user1", "Consulta muy compleja sobre facturación");
        sesion.procesarRespuesta(new RespuestaBot("No lo sé", 0.3));
        
        assertEquals(EstadoSesion.ESCALADA_A_HUMANO, sesion.getEstado());
        assertEquals("No he entendido completamente tu consulta. Te transferiré con un agente.", sesion.getRespuestaActualBot());
    }

    @Test
    void debeValidarRangoConfianza() {
        assertThrows(IllegalArgumentException.class, () -> new RespuestaBot("hola", -0.1));
        assertThrows(IllegalArgumentException.class, () -> new RespuestaBot("hola", 1.1));
    }
}
