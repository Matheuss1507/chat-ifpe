package br.com.ifpe.chat.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import br.com.ifpe.chat.dto.MensagemDTO;

@Controller
public class ChatController {

    @MessageMapping("/chat.enviar/{periodo}")
    @SendTo("/topic/periodo/{periodo}")
    public MensagemDTO enviarMensagem(@DestinationVariable Integer periodo, MensagemDTO mensagem) {
        mensagem.setPeriodo(periodo);
        return mensagem;
    }
}