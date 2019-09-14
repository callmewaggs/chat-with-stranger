package com.chatwithstranger.demo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

// WebSocket message model
@Getter
@Setter
@Entity
public class Message {
    public static final String ENTER = "ENTER";
    public static final String SPEAK = "CHAT";
    public static final String QUIT = "LEAVE";

    @Id
    @Column(nullable = false)
    @JSONField(name = "USERNAME")
    private String username;
    @Column(nullable = false)
    @JSONField(name = "TYPE")
    private String type;
    @Column(nullable = false)
    @JSONField(name = "MSG")
    private String content;
    @Column(nullable = false)
    @JSONField(name = "TIME")
    private String time;
    @Column(nullable = false)
    @JSONField(name = "ONLINECOUNT")
    private int onlineCount;

    private Message() {
    }

    private Message(String username, String type, String content, String time, int onlineCount) {
        this.username = username;
        this.type = type;
        this.content = content;
        this.time = time;
        this.onlineCount = onlineCount;
    }

    public static String jsonConverter(String username, String type, String content, String time, int onlineCount) {
        return JSON.toJSONString(new Message(username, type, content, time, onlineCount));
    }

    public static Message createWithAllArgs(String username, String type, String content, String time, int onlineCount) {
        return new Message(username, type, content, time, onlineCount);
    }
}
