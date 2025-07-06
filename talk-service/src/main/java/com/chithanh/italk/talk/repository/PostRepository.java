package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Post;
import com.chithanh.italk.talk.domain.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @EntityGraph(attributePaths = {"submission", "submission.challenge"})
    Page<Post> findByType(PostType type, Pageable pageable);

    boolean existsBySubmissionId(UUID submissionId);
}