package com.chatwithstranger.demo.service;

import com.chatwithstranger.demo.message.Message;
import com.chatwithstranger.demo.message.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Optional<Message> findMessageByWord(String word) {
        return Optional.empty();
    }
}
