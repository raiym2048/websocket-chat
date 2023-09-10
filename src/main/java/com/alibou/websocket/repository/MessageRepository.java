package com.alibou.websocket.repository;

import com.alibou.websocket.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipient(String sender, String recipient);
   // List<Message> findByRecipientAndIsRead()
}