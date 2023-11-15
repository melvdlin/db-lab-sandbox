package org.somevand.dblabsandbox;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint(value = "/basicEndpoint")
public class BasicEndpoint {

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            session.getBasicRemote().sendText("No");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(message);
    }
}
