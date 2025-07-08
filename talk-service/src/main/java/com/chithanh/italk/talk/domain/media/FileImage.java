package com.chithanh.italk.talk.domain.media;

import com.chithanh.italk.common.domain.AbstractEntity;
import com.chithanh.italk.talk.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_image")
public class FileImage extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition = "text")
    private String url;

    @Column(nullable = false)
    private long size;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}