package org.somevand.dblabsandbox.chat;

import jakarta.json.Json;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

import java.io.StringReader;

public class ClientChatMessageDecoder implements Decoder.Text<ClientChatMessage> {

    @Override
    public ClientChatMessage decode(String message) throws DecodeException {
        try {
            var json = Json.createReader(new StringReader(message)).readObject();
            return new ClientChatMessage(json.getString("body"));
        } catch (Exception e) {
            throw new DecodeException(message, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String message) {
        try {
            var json = Json.createReader(new StringReader(message)).readObject();
            return json.containsKey("body");
        } catch (Exception ignored) {
            return false;
        }
    }
}
