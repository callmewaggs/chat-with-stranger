package com.chatwithstranger.demo.message;

import com.alibaba.fastjson.annotation.JSONField;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

// WebSocket message model
@Getter
@Setter
@Entity
public class Message {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @JSONField(name = "USERNAME")
  private String username;

  @Column(nullable = false)
  @JSONField(name = "TYPE")
  private String type;

  @Column
  @JSONField(name = "MSG")
  private String content;

  @Column(nullable = false)
  @JSONField(name = "TIME")
  private String time;

  @Column(nullable = false)
  @JSONField(name = "ONLINECOUNT")
  private int onlineCount;

  @Column(nullable = false)
  @JSONField(name = "ONLINEUSERS")
  private String onlineUsers;

  protected Message() {
  }

  // TODO : 하드코딩 된 type 을 상수 static 클래스로 빼서.
  // TODO : 이미지 업로드 - 파일 업로드!!!!!
  protected Message(
      String username,
      String type,
      String content,
      String time,
      int onlineCount,
      String onlineUsers) {
    this.username = username;
    this.type = type;
    this.content = content;
    this.time = time;
    this.onlineCount = onlineCount;
    this.onlineUsers = onlineUsers;
  }
}
