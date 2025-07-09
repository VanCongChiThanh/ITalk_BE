package com.chithanh.italk.chat.domain;

import com.chithanh.italk.common.domain.AbstractEntity;
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
@Table(name = "communitys")
public class Community extends AbstractEntity {
      @Id @GeneratedValue private UUID id;
      @Column(nullable = false)
      private String name;
      private UUID ownerId;
      private String description;
      private String avatarUrl;
      private String coverUrl;
      private String slug;
      private boolean isPublic;
}