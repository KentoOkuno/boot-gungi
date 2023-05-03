package gungi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.nio.file.AccessDeniedException;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {
//    @Autowired
//    private WebSocketMessageHandler webSocketMessageHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 相手先
        config.enableSimpleBroker("/client");
        // 自分
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // コネクションを確立するためのエンドポイント
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
//        registry.addEndpoint("/gs-guide-websocket").setHandshakeHandler(handshakeHandler()).withSockJS();
    }

    @Bean
    public HandshakeHandler handshakeHandler() throws AccessDeniedException {
        return new DefaultHandshakeHandler() {

//            @Override protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
////                String user = authHandler.getAuthenticatedUser(request);
//                String user = webSocketMessageHandler.getAuthenticatedUser(request);
//                if (user == null) {
//                    throw new AccessDeniedException("Access denied");
//                }
//                return new UsernamePasswordAuthenticationToken(user, null);
//            }

        };
    }

}