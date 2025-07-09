package com.chithanh.italk.chat.repository;

import com.chithanh.italk.chat.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM messages m WHERE m.communityId = :communityId AND m.createdAt < :before ORDER BY m.createdAt DESC")
    List<Message> findRecentMessages(@Param("communityId") UUID communityId,
                                     @Param("before") Instant before,
                                     Pageable pageable);
}