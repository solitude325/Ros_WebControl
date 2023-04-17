package com.solitude.ros_webserver.ws;

import com.solitude.ros_webserver.config.GetHttpSessionConfig;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
@ServerEndpoint(value = "/server", configurator = GetHttpSessionConfig.class)
public class WebEndpoint {

//    private static final Map<Session, WebSocketClient> onlineUser = new ConcurrentHashMap<>();

    private HttpSession httpSession;

    private WebSocketClient webSocketClient;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws URISyntaxException {
        //Save Session
        //Connect Ros
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String target = (String) this.httpSession.getAttribute("target");
        log.info(target);
        webSocketClient = new WebSocketClient(new URI("ws://" + target + ":9090")) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                //Sub All Topic
                log.info("Connect to Ros(" + target + ") success");
            }

            @Override
            public void onMessage(String s) {
                session.getAsyncRemote().sendText(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                //Send Message
            }

            @Override
            public void onError(Exception e) {
            }
        };
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(String message) {

    }
}
