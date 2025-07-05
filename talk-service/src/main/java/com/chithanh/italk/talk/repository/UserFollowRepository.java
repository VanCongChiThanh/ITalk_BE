package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UUID> {
    Optional<UserFollow> findByFollowingIdAndFollowerId(UUID followerId, UUID followingId);
    Page<UserFollow> findByFollowingId(UUID userId, Pageable pageable);

    Page<UserFollow> findByFollowerId(UUID userId, Pageable pageable);
}