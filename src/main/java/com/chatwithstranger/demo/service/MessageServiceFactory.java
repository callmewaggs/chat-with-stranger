package com.chatwithstranger.demo.service;

import org.springframework.stereotype.Component;

@Component
public class MessageServiceFactory {

    private static MessageService messageService;

    private MessageServiceFactory(MessageService messageService) {
        MessageServiceFactory.messageService = messageService;
    }

    public static MessageService create() {
        return messageService;
    }
}
