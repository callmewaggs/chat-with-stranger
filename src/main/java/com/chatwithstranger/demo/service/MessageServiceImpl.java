package com.chatwithstranger.demo.service;

import com.chatwithstranger.demo.message.Message;
import com.chatwithstranger.demo.message.MessageRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  public MessageServiceImpl(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public void saveMessage(Message message) {
    messageRepository.saveAndFlush(message);
  }

  @Override
  public Optional<Message> findMessageByWord(String word) {
    return Optional.empty();
  }
}
