package com.alibou.websocket.chat;

import com.alibou.websocket.entities.Message;
import com.alibou.websocket.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class ChatController {

    private final MessageRepository messageRepository;

    @GetMapping("/history")
    public List<Message> getChatHistory(@RequestParam String token, @RequestParam String recipient) {
        return messageRepository.findBySenderAndRecipient(token, recipient);
    }

    @GetMapping("/getAll")
    public List<Message> getAll(){
        return messageRepository.findAll();
    }
//    @GetMapping("/unreadMessages")
//    public List<Message> getUnreadMessages(@RequestParam String recipient) {
//        return messageRepository.findByRecipientAndIsRead(recipient, false);
//    }



    @MessageMapping("/sendMessage")
    public Message broadcastMessage(Message message) {
        message.setTime(LocalDateTime.now().toString());
        messageRepository.save(message);

        // Здесь вы можете дополнительно обрабатывать отправку сообщения, если это необходимо
        return message;
    }

    @MessageMapping("/receiveMessage")
    @SendTo("/topic/messages")
    public Message receiveMessage(@Payload Message message) {
        // Здесь вы можете дополнительно обрабатывать полученное сообщение, если это необходимо
        return message;
    }



}
