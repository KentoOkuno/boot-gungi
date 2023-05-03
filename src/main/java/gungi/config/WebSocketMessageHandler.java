package gungi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WebSocketMessageHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public String getAuthenticatedUser(ServerHttpRequest request) {
        // 認証済みのユーザー名を取得するための処理
        // とりあえず仮でsessionIDをそのまま返す
        return Objects.requireNonNull(request.getPrincipal()).getName();
    }

}