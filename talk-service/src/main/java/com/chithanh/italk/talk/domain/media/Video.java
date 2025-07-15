package com.chithanh.italk.talk.domain.media;

import com.chithanh.italk.talk.domain.enums.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String mediaUrl;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private VideoStatus status; // UPLOADED, TRANSCODING, READY, FAILED

    private Instant expiryTime;
}