package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    List<Submission> findAllByUserId(UUID userId);
    @Query("SELECT s.challenge.id FROM Submission s WHERE s.user.id = :userId")
    List<UUID> findChallengeIdsByUserId(@Param("userId") UUID userId);
    @Query("SELECT s FROM Submission s " +
            "WHERE s.user.id = :userId " +
            "AND s.createdAt BETWEEN :startOfDay AND :endOfDay")
    Optional<Submission> findSubmissionToday(
            @Param("userId") UUID userId,
            @Param("startOfDay") Timestamp startOfDay,
            @Param("endOfDay") Timestamp endOfDay
    );






}