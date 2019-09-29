package com.chatwithstranger.demo.message;

import javax.persistence.Entity;

@Entity
public class EnterMessage extends Message {
    private EnterMessage() {
    }

    public EnterMessage(String username, String time, int onlineCount, String onlineUsers) {
        super(username, "ENTER", "", time, onlineCount, onlineUsers);
    }
}
