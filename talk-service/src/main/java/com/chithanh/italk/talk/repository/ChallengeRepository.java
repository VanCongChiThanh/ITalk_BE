package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
    List<Challenge> findByIdNotIn(List<UUID> completedChallengeIds);
    // Custom query methods can be defined here if needed
    @Query("SELECT s.challenge.id FROM Submission s WHERE s.user.id = :userId")
    List<UUID> findChallengeIdsByUserId(@Param("userId") UUID userId);
}