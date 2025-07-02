package com.chithanh.italk.talk.mapper;

import com.chithanh.italk.common.config.SpringMapStructConfig;
import com.chithanh.italk.talk.domain.Submission;
import com.chithanh.italk.talk.payload.response.SubmissionResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface SubmissionMapper {
    SubmissionResponse toSubmissionResponse(Submission submission);
}