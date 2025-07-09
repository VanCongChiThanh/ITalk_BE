package com.chithanh.italk.chat.domain;

import com.chithanh.italk.chat.domain.enums.CommunityRole;
import com.chithanh.italk.common.domain.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "community_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"communityId", "userId"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityMember extends AbstractEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID communityId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityRole role;

    private Timestamp joinedAt;

}