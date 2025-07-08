package com.chithanh.italk.talk.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.mapper.ChallengeMapper;
import com.chithanh.italk.talk.payload.request.ChallengeRequest;
import com.chithanh.italk.talk.payload.response.ChallengeResponse;
import com.chithanh.italk.talk.repository.ChallengeRepository;
import com.chithanh.italk.talk.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    @Override
    public ChallengeResponse createChallenge(ChallengeRequest request) {
        Challenge challenge= new Challenge();
        challenge.setQuestion(request.getQuestion());
        return challengeMapper.toChallengeResponse(challengeRepository.save(challenge));
    }
    @Override
    public List<ChallengeResponse> createChallenges(List<ChallengeRequest> requests) {
        List<Challenge> challenges = requests.stream()
                .map(request -> {
                    Challenge challenge = new Challenge();
                    challenge.setQuestion(request.getQuestion());
                    return challenge;
                })
                .collect(Collectors.toList());
        List<Challenge> savedChallenges = challengeRepository.saveAll(challenges);
        return savedChallenges.stream()
                .map(challengeMapper::toChallengeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChallengeResponse updateChallenge(UUID challengeId, ChallengeRequest request) {
        Challenge challenge = this.findById(challengeId);
        challenge.setQuestion(request.getQuestion());
        return challengeMapper.toChallengeResponse(challengeRepository.save(challenge));
    }

    @Override
    public Challenge findById(UUID challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.CHALLENGE_NOT_FOUND));
    }


    @Override
    public ChallengeResponse getRandomChallenge(UUID userId,List<UUID> completedChallengeIds) {
        Challenge challenge;

        if (completedChallengeIds.isEmpty()) {
            challenge= challengeRepository.findRandomChallenge();
        } else {
            challenge = challengeRepository.findRandomChallengeExcluding(completedChallengeIds);
        }
        if (challenge == null) {
            throw new NotFoundException(MessageConstant.ALL_CHALLENGES_COMPLETED);
        }
        return challengeMapper.toChallengeResponse(challenge);
    }

    @Override
    public Page<ChallengeResponse> getAllChallenges(Pageable pageable) {
        Page<Challenge> pages = challengeRepository.findAll(pageable);
        return pages.map(challengeMapper::toChallengeResponse);
    }


}