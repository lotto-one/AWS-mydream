package kr.ict.mydream.chat;

import org.springframework.web.socket.server.HandshakeInterceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String protocol = request.getHeaders().getFirst("Sec-WebSocket-Protocol");
        // int Intprotocol = Integer.parseInt(protocol);
        if (protocol != null) {
            response.getHeaders().set("Sec-WebSocket-Protocol", protocol);
            attributes.put("protocol", protocol);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception ex) {
        // 아무것도 하지 않음
    }
}
