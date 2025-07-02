package com.chithanh.italk.talk.service;


import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.payload.request.ChallengeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ChallengeService {
    Challenge createChallenge(ChallengeRequest request);
    Challenge updateChallenge(UUID challengeId, ChallengeRequest request);
    Challenge findById(UUID challengeId);
    Challenge getRandomChallenge(UUID userId);
    Page<Challenge> getAllChallenges(Pageable pageable);
    List<UUID> findChallengeIdsByUserId(UUID userId);
}