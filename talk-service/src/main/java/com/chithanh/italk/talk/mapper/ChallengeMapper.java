package com.chithanh.italk.talk.mapper;

import com.chithanh.italk.common.config.SpringMapStructConfig;
import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.payload.response.ChallengeResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface ChallengeMapper {
    ChallengeResponse toChallengeResponse(Challenge challenge);
}