package gungi.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TextWebSocketHandler extends org.springframework.web.socket.handler.TextWebSocketHandler {

    private final Map<String, Set<WebSocketSession>> userSessionsMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        userSessionsMap.computeIfAbsent(userId, key -> new HashSet<>()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle messages here
        sendMessageToUser(getUserIdFromSession(session), message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        Set<WebSocketSession> sessions = userSessionsMap.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                userSessionsMap.remove(userId);
            }
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        // とりあえずDBは使用せず、userId == sessionIdとする
        return session.getId();
    }

    public void sendMessageToUser(String userId, TextMessage message) {
        Set<WebSocketSession> sessions = userSessionsMap.get(userId);

        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        // Handle exception
                    }
                }
            }
        }
    }
}
