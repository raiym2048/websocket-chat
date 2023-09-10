package com.alibou.websocket.config;


import com.alibou.websocket.entities.Message;
import com.alibou.websocket.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyHandler implements WebSocketHandler {
    private Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();


    @Autowired
    private MessageRepository messageRepository;

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // Здесь вы можете, например, извлечь токен или имя пользователя из параметров сессии и добавить их в вашу карту.
        Object senderObj = session.getAttributes().get("sender");
        System.out.println("Setting sender attribute for session. Sender: " + senderObj);

        if(senderObj != null) {
            String sender = senderObj.toString();
            userSessions.put(sender, session);
        }

        System.out.println("\n\nConnection established from: " + session.getRemoteAddress());

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        // Преобразование сообщения и сохранение
        String payload = (String) webSocketMessage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(payload, Message.class);

        System.out.println("Message received from " + message.getSender() + " to " + message.getRecipient());

        WebSocketSession recipientSession = userSessions.get(message.getRecipient());
        if (recipientSession != null) {
            System.out.println("Recipient session found for " + message.getRecipient());
        } else {
            System.out.println("No session found for recipient " + message.getRecipient());
        }

        if (recipientSession != null && recipientSession.isOpen()) {
            System.out.println("Sending message to " + message.getRecipient());
            recipientSession.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(message)));
        } else {
            System.out.println("Recipient's session is not open. Storing message.");
            messageRepository.save(message);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Добавьте логирование ошибок здесь, например:
        System.err.println("Error occurred with user: " + session.getAttributes().get("sender"));

        exception.printStackTrace();
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = (String) session.getAttributes().get("sender");
        userSessions.remove(userId);
    }


    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // ... другие методы
}



