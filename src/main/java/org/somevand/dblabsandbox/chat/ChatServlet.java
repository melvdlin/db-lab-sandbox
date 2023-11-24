package org.somevand.dblabsandbox.chat;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebServlet(name = "chatServlet", urlPatterns = "/chat/*")
public class ChatServlet extends HttpServlet {

    private static final MustacheFactory MUSTACHE_FACTORY = new DefaultMustacheFactory();
    private static final ConcurrentMap<String, Mustache> MUSTACHES = new ConcurrentHashMap<>();

    @Override
    public void init() {
        var templateStream =
                getServletContext().getResourceAsStream("/templates/chat.mustache");
        var templateReader = new InputStreamReader(templateStream);
        MUSTACHES.put("chat", MUSTACHE_FACTORY.compile(templateReader, "chat"));
    }

    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        var username = request.getPathInfo().substring(1);

        if (username.isEmpty()) {
            response.sendError(404);
        }

        var endpointURL = getEndpointURL(request, username);

        var templateContext = Map.of(
                "endpointURL", endpointURL,
                "username", username
        );


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out = new PrintWriter(MUSTACHES.get("chat").execute(out, templateContext));
    }

    private static String getEndpointURL(
            HttpServletRequest request,
            String username) {
        var websocketProtocol =
                request.isSecure() ? "wss" : "ws";
        var host = request.getServerName();
        var port = request.getServerPort();
        var context = request.getContextPath().substring(1);
        var endpoint = "endpoints/chat";
        return String.format(
                "%s://%s:%d/%s/%s/%s",
                websocketProtocol,
                host,
                port,
                context,
                endpoint,
                username);
    }
}