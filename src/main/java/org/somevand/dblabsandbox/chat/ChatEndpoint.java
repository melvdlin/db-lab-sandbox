package org.somevand.dblabsandbox.chat;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value = "/endpoints/chat/{username}",
        encoders = {ChatMessageEncoder.class},
        decoders = {ClientChatMessageDecoder.class})
public class ChatEndpoint {

    private static final Set<Session> chatters = new CopyOnWriteArraySet<>();

    public ChatEndpoint() {

    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        chatters.add(session);
        broadcast(new ChatMessage(username, "joined!"));
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        chatters.remove(session);
        broadcast(new ChatMessage(username, "left!"));
    }

    @OnMessage
    public void onMessage(
            ClientChatMessage message,
            @PathParam("username") String username) {
        broadcast(new ChatMessage(username, message.body()));
    }

    private static void broadcast(ChatMessage message) {
        try {
            for (var chatter : chatters) {
                chatter.getBasicRemote().sendObject(message);
            }
        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
    }
}
