package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    List<Submission> findAllByUserId(UUID userId);
    @Query("SELECT s.challenge.id FROM Submission s WHERE s.user.id = :userId")
    List<UUID> findChallengeIdsByUserId(@Param("userId") UUID userId);
}