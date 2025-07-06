package com.chithanh.italk.talk.payload.request;

import com.chithanh.italk.talk.domain.enums.PostType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostRequest {
    @NotNull private PostType type;
    private String content;
    private UUID submissionId;
}