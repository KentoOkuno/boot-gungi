package gungi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class TextWebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private TextWebSocketHandler textWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // コネクションを確立するためのエンドポイント
        registry.addHandler(textWebSocketHandler, "/ws").setAllowedOrigins("*");
    }
}