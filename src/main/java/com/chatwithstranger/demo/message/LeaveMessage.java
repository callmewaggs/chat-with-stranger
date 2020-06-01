package com.chatwithstranger.demo.message;

import javax.persistence.Entity;

@Entity
public class LeaveMessage extends Message {

  private LeaveMessage() {
  }

  public LeaveMessage(String username, String time, int onlineCount, String onlineUsers) {
    super(username, "LEAVE", "", time, onlineCount, onlineUsers);
  }
}
