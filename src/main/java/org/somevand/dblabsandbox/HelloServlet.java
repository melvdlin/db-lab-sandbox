package org.somevand.dblabsandbox;

import com.github.mustachejava.DefaultMustacheFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        var contextPath = getServletContext().getContextPath();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        var mustacheFactory = new DefaultMustacheFactory();
        var scriptedMustache =
                mustacheFactory.compile(new InputStreamReader(
                        getServletContext().getResourceAsStream(
                                "templates/scripted.mustache")), "scripted");

        var context = Map.of("contextPath", contextPath);

        out = new PrintWriter(scriptedMustache.execute(out, context));
    }

    public void destroy() {
    }
}