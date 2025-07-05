package com.chithanh.italk.talk.domain;

import com.chithanh.italk.common.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_follows")
public class UserFollow extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private UUID followerId;
    @Column(nullable = false)
    private UUID followingId;
    private Timestamp followedAt = Timestamp.from(Instant.now());

    @PrePersist
    public void prePersist() {
        this.followedAt = Timestamp.from(Instant.now());
    }
}