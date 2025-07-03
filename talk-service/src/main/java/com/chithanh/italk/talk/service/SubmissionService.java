package com.chithanh.italk.talk.service;

import com.chithanh.italk.talk.domain.Submission;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface SubmissionService {
    List<Submission> findSubmissionsByUserId(UUID userId);
    Submission submit(UUID challengeId, UUID userId, MultipartFile audioFile);
    List<UUID> findChallengeIdsByUserId(UUID userId);
}