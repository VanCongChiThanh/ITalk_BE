package com.chithanh.italk.talk.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.payload.request.ChallengeRequest;
import com.chithanh.italk.talk.repository.ChallengeRepository;
import com.chithanh.italk.talk.service.ChallengeService;
import com.chithanh.italk.talk.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;

    @Override
    public Challenge createChallenge(ChallengeRequest request) {
        Challenge challenge= new Challenge();
        challenge.setQuestion(request.getQuestion());
        return challengeRepository.save(challenge);
    }

    @Override
    public Challenge updateChallenge(UUID challengeId, ChallengeRequest request) {
        Challenge challenge = this.findById(challengeId);
        challenge.setQuestion(request.getQuestion());
        return challengeRepository.save(challenge);
    }

    @Override
    public Challenge findById(UUID challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.CHALLENGE_NOT_FOUND));
    }

    @Override
    public Challenge getRandomChallenge(UUID userId) {
        //get challenges has been submitted at least once
        List<UUID> completedChallengeIds = this.findChallengeIdsByUserId(userId);
        List<Challenge> remainingChallenges;

        if (completedChallengeIds.isEmpty()) {
            remainingChallenges = challengeRepository.findAll();
        } else {
            remainingChallenges = challengeRepository.findByIdNotIn(completedChallengeIds);
        }
        if (remainingChallenges.isEmpty()) {
            throw new NotFoundException(MessageConstant.ALL_CHALLENGES_COMPLETED);
        }
        Random random = new Random();
        return remainingChallenges.get(random.nextInt(remainingChallenges.size()));
    }

    @Override
    public Page<Challenge> getAllChallenges(Pageable pageable) {
        return challengeRepository.findAll(pageable);
    }

    @Override
    public List<UUID> findChallengeIdsByUserId(UUID userId) {
        return challengeRepository.findChallengeIdsByUserId(userId);
    }

}