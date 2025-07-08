package com.chithanh.italk.talk.service;


import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.payload.request.ChallengeRequest;
import com.chithanh.italk.talk.payload.response.ChallengeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ChallengeService {
    ChallengeResponse createChallenge(ChallengeRequest request);
    List<ChallengeResponse> createChallenges(List<ChallengeRequest> requests);
    ChallengeResponse updateChallenge(UUID challengeId, ChallengeRequest request);
    Challenge findById(UUID challengeId);
    Page<ChallengeResponse> getAllChallenges(Pageable pageable);
    ChallengeResponse getRandomChallenge(UUID userId,List<UUID> completedChallengeIds);
}