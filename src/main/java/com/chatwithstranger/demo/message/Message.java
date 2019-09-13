package com.chatwithstranger.demo.message;

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
    @Id
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String time;

    private Message() {
    }

    private Message(String username, String content, String time) {
        this.username = username;
        this.content = content;
        this.time = time;
    }

    public static Message createWithAllArgs(String username, String content, String time) {
        return new Message(username, content, time);
    }
}
