package com.chatwithstranger.demo.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  Optional<Message> findMessageByContent(String content);
}
