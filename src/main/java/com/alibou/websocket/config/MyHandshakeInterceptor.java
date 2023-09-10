package com.alibou.websocket.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class MyHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Извлеките параметр "user" из запроса
        //UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(request.getURI());
        String query = request.getURI().getQuery();
        if (query != null && query.contains("user=")) {
            String user = query.split("user=")[1];
            attributes.put("sender", user);
        } else {
            // Если у вас нет параметра user, возможно, вы захотите обработать это иначе.
        }

        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Постобработка рукопожатия. Обычно здесь ничего делать не нужно.
    }
}

