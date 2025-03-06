package com.emotionalcart.infra;

import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.WebSocket;

import java.io.UncheckedIOException;

public class CustomHttpClient implements HttpClient {

    @Override public WebSocket openSocket(HttpRequest request, WebSocket.Listener listener) {
        request.setHeader("Connection", "close"); // HTTP/1.1 강제 적용
        return null;
    }

    @Override public HttpResponse execute(HttpRequest req) throws UncheckedIOException {
        req.setHeader("Connection", "close"); // HTTP/1.1 강제 적용
        return null;
    }

}