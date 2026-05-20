package com.openlib.market.application.chatbot;

import com.openlib.market.domain.chatbot.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcesarMensajeChatbotInteractorTest {

    private IChatbotGateway chatbotGateway;
    private ITicketSoporteGateway ticketGateway;
    private ProcesarMensajeChatbotInteractor interactor;

    @BeforeEach
    void setUp() {
        chatbotGateway = mock(IChatbotGateway.class);
        ticketGateway = mock(ITicketSoporteGateway.class);
        interactor = new ProcesarMensajeChatbotInteractor(chatbotGateway, ticketGateway);
    }

    @Test
    void debeResponderNormalmenteSiConfianzaEsAlta() {
        when(chatbotGateway.consultar("ayuda")).thenReturn(new RespuestaBot("Aquí tienes ayuda", 0.9));

        String respuesta = interactor.procesarMensaje("u1", "ayuda");

        assertEquals("Aquí tienes ayuda", respuesta);
        verify(ticketGateway, never()).crearTicket(any(), any());
    }

    @Test
    void debeEscalarYCrearTicketSiConfianzaEsBaja() {
        when(chatbotGateway.consultar("problema super raro")).thenReturn(new RespuestaBot("No sé", 0.4));

        String respuesta = interactor.procesarMensaje("u1", "problema super raro");

        assertEquals("No he entendido completamente tu consulta. Te transferiré con un agente.", respuesta);
        verify(ticketGateway, times(1)).crearTicket("u1", "problema super raro");
    }
}
