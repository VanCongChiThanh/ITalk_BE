package com.chithanh.italk.talk.repository;

import com.chithanh.italk.talk.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    List<Submission> findAllByUserId(UUID userId);
}