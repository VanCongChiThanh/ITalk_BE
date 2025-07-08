package com.chithanh.italk.talk.domain;

import com.chithanh.italk.talk.domain.media.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reels")
public class Reel {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    private Integer duration; // Duration in seconds
}