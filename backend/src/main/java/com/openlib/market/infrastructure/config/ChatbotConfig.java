package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.chatbot.IProcesarMensajeChatbotUseCase;
import com.openlib.market.application.chatbot.ProcesarMensajeChatbotInteractor;
import com.openlib.market.domain.chatbot.IChatbotGateway;
import com.openlib.market.domain.chatbot.ITicketSoporteGateway;

@Configuration
public class ChatbotConfig {

    @Bean
    public IProcesarMensajeChatbotUseCase procesarMensajeChatbotUseCase(
            IChatbotGateway chatbotGateway,
            ITicketSoporteGateway ticketGateway
    ) {
        return new ProcesarMensajeChatbotInteractor(chatbotGateway, ticketGateway);
    }
}