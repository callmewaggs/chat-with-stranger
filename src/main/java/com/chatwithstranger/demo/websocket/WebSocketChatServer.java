package com.chatwithstranger.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/open")    // open 이라는 Url 요청을 통해 웹소켓에 들어가겠다.
public class WebSocketChatServer {
    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatServer.class);

    public WebSocketChatServer() {
        System.out.println("WebSocketChatServer object created");
    }

    // send message method.
    private static void sendMessageToAll(String msg) {
        try {
            for (Session session : onlineSessions.values()) {
                session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            // TODO : handle exception.
            e.printStackTrace();
        }
    }

    // Open connection, 1) add session, 2) add user.
    // onOpen : on open connection to establish the connection.
    // 클라이언트가 웹소켓에 들어오고 서버에 아무런 문제 없이 들어왔을 때 실행되는 메서드
    @OnOpen
    public void onOpen(Session session) {
        logger.info("Open session id : " + session.getId());
        try {
            final Basic basic = session.getBasicRemote();
            basic.sendText("Connection Established");
        } catch (IOException e) {
            // TODO : handle exception.
            e.printStackTrace();
        }
        onlineSessions.put(session.getId(), session);
    }

    // onMessage: 1) Get username and session. 2) Send message to all.
    // 클라이언트에게 메세지가 들어왔을 때 실행되는 메서드.
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        String[] parsed = jsonStr.split(":");
        logger.info("Message from " + parsed[0] + " : " + parsed[1]);
        try {
            final Basic basic = session.getBasicRemote();
            basic.sendText("to : " + jsonStr);
        } catch (IOException e) {
            // TODO : handle exception.
            e.printStackTrace();
        }
        sendMessageToAll(jsonStr);
    }

    // Close connection, 1) remove session, 2) update user.
    // 클라이언트와 웹소켓의 연결이 끊기면 실행되는 메서드.
    @OnClose
    public void onClose(Session session) {
        logger.info("Session " + session.getId() + " has ended");
        onlineSessions.remove(session.getId());
    }

    // Print exception.
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("Session " + session.getId() + " has error");
        error.printStackTrace();
    }

}