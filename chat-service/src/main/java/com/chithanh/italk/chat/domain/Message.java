package com.chithanh.italk.chat.domain;

import com.chithanh.italk.chat.domain.enums.MessageType;
import com.chithanh.italk.common.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID senderId;
    private UUID communityId;
    private String content;
    private MessageType type;
}