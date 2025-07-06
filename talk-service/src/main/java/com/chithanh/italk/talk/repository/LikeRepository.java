package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    // This repository can be used to manage likes on posts or comments.
    // You can add custom query methods here if needed.
}