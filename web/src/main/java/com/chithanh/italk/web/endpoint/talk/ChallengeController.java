package com.chithanh.italk.web.endpoint.talk;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.talk.domain.Challenge;
import com.chithanh.italk.talk.mapper.ChallengeMapper;
import com.chithanh.italk.talk.payload.request.ChallengeRequest;
import com.chithanh.italk.talk.payload.response.ChallengeResponse;
import com.chithanh.italk.talk.service.ChallengeService;
import com.chithanh.italk.talk.service.SubmissionService;
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
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;
    private final SubmissionService submissionService;
    private final ChallengeMapper challengeMapper;

    @GetMapping("/challenges/all")
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
    @GetMapping("/users/{userId}/challenges/random")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Get a random challenge for the user")
    public ResponseEntity<ResponseDataAPI> getRandomChallengeForUser(
            @PathVariable UUID userId
    ) {
        List<UUID> completedChallengeIds = submissionService.findChallengeIdsByUserId(userId);
        Challenge challenge = challengeService.getRandomChallenge(userId, completedChallengeIds);
        ChallengeResponse response = challengeMapper.toChallengeResponse(challenge);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta( response));
    }
    @PostMapping("/challenges")
    @ApiOperation("create a new challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDataAPI> createChallenge(
            @RequestBody ChallengeRequest challengeRequest
    ) {
        Challenge createdChallenge = challengeService.createChallenge(challengeRequest);
        ChallengeResponse response = challengeMapper.toChallengeResponse(createdChallenge);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
    }
    @PatchMapping("/challenges/{challengeId}")
    @ApiOperation("update a challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDataAPI> updateChallenge(
            @PathVariable UUID challengeId,
            @RequestBody ChallengeRequest challengeRequest
    ) {
        Challenge updatedChallenge = challengeService.updateChallenge(challengeId, challengeRequest);
        ChallengeResponse response = challengeMapper.toChallengeResponse(updatedChallenge);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
    }

}