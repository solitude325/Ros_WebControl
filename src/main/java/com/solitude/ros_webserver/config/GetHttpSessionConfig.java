package com.solitude.ros_webserver.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        //get HTTPSession
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        //save HTTPSession
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
