package com.chithanh.italk.user.domain;

import com.chithanh.italk.common.domain.AbstractEntity;
import com.chithanh.italk.user.domain.enums.Gender;
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
@Table(name = "user_info")
public class UserInfo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    private String email;

    private String avatar;

    private Gender gender;

    private Boolean canPostReel;

    private Integer streakCount;

}