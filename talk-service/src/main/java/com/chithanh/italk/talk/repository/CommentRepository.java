package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Comment;
import com.chithanh.italk.talk.domain.enums.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByTargetTypeAndTargetId(TargetType targetType, UUID targetId, Pageable pageable);
}