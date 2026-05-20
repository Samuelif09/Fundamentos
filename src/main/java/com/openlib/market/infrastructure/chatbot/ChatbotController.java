package com.openlib.market.infrastructure.chatbot;

import com.openlib.market.application.chatbot.IProcesarMensajeChatbotUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/soporte/chatbot")
public class ChatbotController {

    private final IProcesarMensajeChatbotUseCase chatbotUseCase;

    public ChatbotController(IProcesarMensajeChatbotUseCase chatbotUseCase) {
        this.chatbotUseCase = chatbotUseCase;
    }

    @PostMapping("/mensajes")
    public ResponseEntity<MensajeRespuesta> enviarMensaje(@RequestBody MensajeRequest request) {
        try {
            String respuesta = chatbotUseCase.procesarMensaje(request.idUsuario(), request.mensaje());
            return ResponseEntity.ok(new MensajeRespuesta(respuesta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getMessage()));
        }
    }

    public record MensajeRequest(String idUsuario, String mensaje) {}
    public record MensajeRespuesta(String respuesta) {}
}
