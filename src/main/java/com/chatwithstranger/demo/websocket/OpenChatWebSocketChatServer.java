package com.chatwithstranger.demo.websocket;

import com.alibaba.fastjson.JSON;
import com.chatwithstranger.demo.message.EnterMessage;
import com.chatwithstranger.demo.message.LeaveMessage;
import com.chatwithstranger.demo.message.Message;
import com.chatwithstranger.demo.message.NoticeMessage;
import com.chatwithstranger.demo.service.MessageServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/open/{username}") // open 이라는 Url 요청을 통해 웹소켓에 들어가겠다.
public class OpenChatWebSocketChatServer {
    private static final Logger logger = LoggerFactory.getLogger(OpenChatWebSocketChatServer.class);
    // All chat sessions.
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");

    // send message method.
    public static void sendMessageToAll(Message message) {

        try {
            String parsed = JSON.toJSONString(message);
            for (Session session : onlineSessions.values()) {
                session.getBasicRemote().sendText(parsed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Open connection, 1) add session, 2) add user.
    // onOpen : on open connection to establish the connection.
    // 클라이언트가 웹소켓에 들어오고 서버에 아무런 문제 없이 들어왔을 때 실행되는 메서드
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        logger.info("Open session id : " + session.getId() + ", username : " + username);

        if (onlineSessions.containsKey(session.getId())) return;

        onlineSessions.put(username, session);
        String onlineUsers = onlineSessions.keySet().toString();
        Message message =
                new EnterMessage(username, sdf.format(new Date()), onlineSessions.size(), onlineUsers);

        saveAndSendMessage(message);
    }

    // onMessage: 1) Get username and session. 2) Send message to all.
    // 클라이언트에게 메세지가 들어왔을 때 실행되는 메서드.
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);
        message.setTime(sdf.format(new Date()));
        message.setOnlineUsers(onlineSessions.keySet().toString());
        message.setOnlineCount(onlineSessions.size());

        if (message.getContent().startsWith("@공지 ")) {
            message =
                    new NoticeMessage(
                            message.getUsername(),
                            message.getContent().replace("@공지 ", ""),
                            message.getTime(),
                            message.getOnlineCount(),
                            message.getOnlineUsers());
        }

        logger.info("Message from " + message.getUsername());
        saveAndSendMessage(message);
    }

    // Close connection, 1) remove session, 2) update user.
    // 클라이언트와 웹소켓의 연결이 끊기면 실행되는 메서드.
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        logger.info("Close session id : " + session.getId() + ", username : " + username);

        onlineSessions.remove(username);
        Message message =
                new LeaveMessage(
                        username,
                        sdf.format(new Date()),
                        onlineSessions.size(),
                        onlineSessions.keySet().toString());

        saveAndSendMessage(message);
    }

    // Print exception.
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    private void saveAndSendMessage(Message message) {
        MessageServiceFactory.getInstance().saveMessage(message);
        sendMessageToAll(message);
    }
}
