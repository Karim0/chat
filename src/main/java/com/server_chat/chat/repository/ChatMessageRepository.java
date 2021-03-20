package com.server_chat.chat.repository;

import com.server_chat.chat.entities.ChatMessage;
import com.server_chat.chat.entities.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);

//    @Query("UPDATE FROM message m WHERE m")
//    void updateMessage(Long senderId, Long recipientId, Long status);
}
