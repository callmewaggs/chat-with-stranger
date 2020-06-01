package com.chatwithstranger.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chatwithstranger.demo.service.MessageService;
import com.chatwithstranger.demo.service.MessageServiceFactory;
import com.chatwithstranger.demo.websocket.WebSocketServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class WebSocketServerTest {

    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private Basic endpoint;
    private Session session;
  private WebSocketServer server;
    private ArgumentCaptor<String> captor;
    private String username;

    @Before
    public void setUp() {
        MessageServiceFactory.init(mock(MessageService.class));
        username = "test";
      server = new WebSocketServer();
        captor = ArgumentCaptor.forClass(String.class);
        endpoint = mock(Basic.class);
        session = createSession(username, endpoint);
    }

    private Session createSession(String id, Basic endpoint) {
        Session session = mock(Session.class);
        when(session.getId()).thenReturn(id);
        when(session.getBasicRemote()).thenReturn(endpoint);
        onlineSessions.put(username, session);
        return session;
    }

    @After
    public void tearDown() {
        onlineSessions.keySet().forEach(key -> server.onClose(onlineSessions.get(key), key));
    }

    @Test
    public void sendENTERMessageOnOpenTest() throws IOException {
        // Act
        server.onOpen(session, username);

        // Assert
        verify(endpoint).sendText(captor.capture());
        assertEquals("ENTER", sentObject().getString("TYPE"));
        assertEquals(1, sentObject().getIntValue("ONLINECOUNT"));
    }

    @Test
    public void sendCHATMessageOnMessageTest() throws IOException {
        Map<String, String> message = new HashMap<>();
        message.put("USERNAME", "test");
        message.put("MSG", "testing a message");
        message.put("TYPE", "CHAT");

        server.onOpen(session, username);
        server.onMessage(session, JSON.toJSONString(message));

        verify(endpoint, times(2)).sendText(captor.capture());
        assertEquals("CHAT", sentObject().getString("TYPE"));
        assertEquals("test", sentObject().getString("USERNAME"));
        assertEquals("testing a message", sentObject().getString("MSG"));
        assertEquals(1, sentObject().getIntValue("ONLINECOUNT"));
    }

    @Test
    public void sendLEAVEMessageOnClose() throws IOException {
        Session anotherSession = createSession("test2", mock(Basic.class));

        server.onOpen(session, username);
        server.onOpen(anotherSession, "test2");
        server.onClose(anotherSession, "test2");

        verify(endpoint, times(3)).sendText(captor.capture());
        assertEquals("LEAVE", sentObject().getString("TYPE"));
        assertEquals(1, sentObject().getIntValue("ONLINECOUNT"));
    }

    private JSONObject sentObject() {
        return JSON.parseObject(captor.getValue());
    }
}
