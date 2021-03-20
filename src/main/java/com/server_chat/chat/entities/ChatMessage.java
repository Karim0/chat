package com.server_chat.chat.entities;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String chatId;
    @Column
    private String senderId;
    @Column
    private String recipientId;
    @Column
    private String senderName;
    @Column
    private String recipientName;
    @Column
    private String content;
    @Column
    private Date timestamp;
    @Column
    private MessageStatus status;
}
