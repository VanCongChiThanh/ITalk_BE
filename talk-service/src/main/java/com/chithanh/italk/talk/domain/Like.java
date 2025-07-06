package com.chithanh.italk.talk.domain;

import com.chithanh.italk.common.domain.AbstractEntity;
import com.chithanh.italk.talk.domain.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private TargetType targetType; // Type of the target (e.g., POST, COMMENT)
    private UUID targetId; // ID of the liked post or comment
}