package org.somevand.dblabsandbox.chat;

import jakarta.json.Json;
import jakarta.websocket.Encoder;

public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {
    @Override
    public String encode(ChatMessage message) {
        return Json.createObjectBuilder()
                   .add("username", message.username())
                   .add("body", message.body())
                   .build()
                   .toString();
    }
}
