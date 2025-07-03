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

    @Query(value = "SELECT * FROM challenges c WHERE c.id NOT IN (:completedIds) ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Challenge findRandomChallengeExcluding(@Param("completedIds") List<UUID> completedIds);

    @Query(value = "SELECT * FROM challenges ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Challenge findRandomChallenge();

}