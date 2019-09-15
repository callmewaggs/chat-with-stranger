package com.chatwithstranger.demo.service;

import com.chatwithstranger.demo.message.Message;

import java.util.Optional;

public interface MessageService {
    void saveMessage(Message message);

    Optional<Message> findMessageByWord(String word);
}
