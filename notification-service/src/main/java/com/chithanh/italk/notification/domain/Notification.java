package com.chithanh.italk.notification.domain;

import com.chithanh.italk.common.domain.AbstractEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Notification extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID receiverId;

    private String position;
    private String type;
    private String action;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String data;

    private boolean isRead;

}