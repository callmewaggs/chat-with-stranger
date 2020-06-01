package com.chatwithstranger.demo.websocket.config;

import com.chatwithstranger.demo.service.MessageService;
import com.chatwithstranger.demo.service.MessageServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModuleConfig {

  @Bean
  public MessageServiceFactory messageServiceFactory(MessageService messageService) {
    MessageServiceFactory messageServiceFactory = new MessageServiceFactory();
    MessageServiceFactory.init(messageService);
    return messageServiceFactory;
  }
}
