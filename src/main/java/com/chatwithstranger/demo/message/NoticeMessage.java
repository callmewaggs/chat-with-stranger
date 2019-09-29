package com.chatwithstranger.demo.message;

import javax.persistence.Entity;

@Entity
public class NoticeMessage extends Message {
    private NoticeMessage() {
    }

    public NoticeMessage(String username, String content, String time, int onlineCount, String onlineUsers) {
        super(username, "NOTICE", content, time, onlineCount, onlineUsers);
    }
}
