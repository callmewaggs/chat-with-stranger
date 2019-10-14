package com.chatwithstranger.demo.message;

import javax.persistence.Entity;

@Entity
public class ResourceMessage extends Message {
    private ResourceMessage() {
    }

    public ResourceMessage(String username, String time, int onlineCount, String onlineUsers, String resource) {
        super(username, "RESOURCE", resource, time, onlineCount, onlineUsers);
    }
}
