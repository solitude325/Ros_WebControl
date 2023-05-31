package com.solitude.ros_webserver.ws;

import com.alibaba.fastjson.JSON;
import com.solitude.ros_webserver.config.GetHttpSessionConfig;
import com.solitude.ros_webserver.ws.pojo.RosMessage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
@ServerEndpoint(value = "/server")
public class WebEndpoint {

//    private static final Map<Session, WebSocketClient> onlineUser = new ConcurrentHashMap<>();

    private HttpSession httpSession;

    private WebSocketClient webSocketClient;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws URISyntaxException {
        //Save Session
        //Connect Ros
//        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        String target = (String) this.httpSession.getAttribute("target");
//        log.info(target);
        webSocketClient = new WebSocketClient(new URI("ws://" + "192.168.31.215" + ":9090")) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                //Sub All Topic
                log.info("Connect to Ros(" + "target" + ") success");
                RosMessage subMsg = new RosMessage();
                subMsg.setOp("subscribe");
                subMsg.setTopic("/map");
                send(JSON.toJSONString(subMsg));
                log.info(JSON.toJSONString(subMsg));
                subMsg.setTopic("/image_raw/compressed");
                send(JSON.toJSONString(subMsg));
                log.info(JSON.toJSONString(subMsg));
            }

            @Override
            public void onMessage(String s) {
                try {
                    session.getBasicRemote().sendText(s);
                    //Basic or Async
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                log.info("Connect to Ros disconnected by Ros");
                //Give user tips
            }

            @Override
            public void onError(Exception e) {
                log.info("Error Ros");
            }
        };
        webSocketClient.connect();
    }

    @OnClose
    public void onClose(Session session) throws InterruptedException {
        RosMessage subMsg = new RosMessage();
        subMsg.setOp("unsubscribe");
        subMsg.setTopic("/map");
        webSocketClient.send(JSON.toJSONString(subMsg));
        log.info(JSON.toJSONString(subMsg));
        subMsg.setTopic("/image_raw/compressed");
        webSocketClient.send(JSON.toJSONString(subMsg));
        log.info(JSON.toJSONString(subMsg));
        Thread.sleep(500);
        webSocketClient.close();
    }

    @OnMessage
    public void onMessage(String message) {
        webSocketClient.send(message);
    }
}
