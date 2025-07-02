package com.chithanh.italk.web.endpoint.talk;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.mapper.ChallengeMapper;
import com.chithanh.italk.talk.payload.response.ChallengeResponse;
import com.chithanh.italk.talk.service.ChallengeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;
    private final ChallengeMapper challengeMapper;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get all challenges")
    public ResponseEntity<ResponseDataAPI> getAllChallenges(
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "10") int paging
    ) {
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<Challenge> pages = challengeService.getAllChallenges(pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, pages.getTotalPages(), pages.getTotalElements());
        List<ChallengeResponse> data = pages.getContent().stream()
                .map(challengeMapper::toChallengeResponse)
                .toList();
        return ResponseEntity.ok(ResponseDataAPI.success(data, pageInfo));
    }
    @GetMapping("/users/{userId}/random")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get a random challenge for the user")
    public ResponseEntity<ResponseDataAPI> getRandomChallengeForUser(
            @PathVariable UUID userId
    ) {
        Challenge challenge = challengeService.getRandomChallenge(userId);
        ChallengeResponse response = challengeMapper.toChallengeResponse(challenge);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta( response));
    }


}