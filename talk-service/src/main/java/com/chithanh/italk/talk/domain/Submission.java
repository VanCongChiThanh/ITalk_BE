package com.chithanh.italk.talk.domain;

import com.chithanh.italk.common.domain.AbstractEntity;
import com.chithanh.italk.security.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submissions")
public class Submission extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String audioUrl;
    private Integer score;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String feedback;

    private Integer likesCount = 0;
}