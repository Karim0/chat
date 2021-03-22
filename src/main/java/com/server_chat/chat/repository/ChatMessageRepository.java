package com.server_chat.chat.repository;

import com.server_chat.chat.entities.ChatMessage;
import com.server_chat.chat.entities.MessageStatus;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    @Query("SELECT m FROM chat_message m WHERE m.chatId = :chatId order by m.timestamp")
    List<ChatMessage> findByChatId(@Param("chatId") String chatId);

    @Modifying
    @Query("UPDATE chat_message c SET c.status = :status WHERE c.senderId = :sender_id AND c.recipientId = :recipient_id")
    void updateMessage(@Param("sender_id") String senderId,
                       @Param("recipient_id") String recipientId,
                       @Param("status") MessageStatus status);
}
