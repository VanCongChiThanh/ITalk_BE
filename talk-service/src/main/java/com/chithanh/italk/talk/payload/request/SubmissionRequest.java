package com.chithanh.italk.talk.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubmissionRequest {
    @NotNull private UUID challengeId;
    @NotNull private String audioUrl;
    @NotNull private Integer duration;
    private Integer score;
    private String feedback;
    private Integer likesCount;
}