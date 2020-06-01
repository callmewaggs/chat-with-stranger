package com.chatwithstranger.demo.service;

public class MessageServiceFactory {

  private static MessageService messageService;

  public static MessageService getInstance() {
    return messageService;
  }

  public static void init(MessageService messageService) {
    //        if (MessageServiceFactory.messageService != null)
    //            throw new IllegalStateException("Not Allow Duplicated Instance");

    MessageServiceFactory.messageService = messageService;
  }
}
