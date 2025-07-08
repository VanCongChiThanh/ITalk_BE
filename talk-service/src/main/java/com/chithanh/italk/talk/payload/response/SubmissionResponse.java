package com.chithanh.italk.talk.payload.response;


import com.chithanh.italk.talk.domain.Submission;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmissionResponse {
    private UUID id;
    private UUID challengeId;
    private String question;
    private UUID userId;
    private String audioUrl;
    private Integer duration;
    private Integer score;
    private String feedback;
}