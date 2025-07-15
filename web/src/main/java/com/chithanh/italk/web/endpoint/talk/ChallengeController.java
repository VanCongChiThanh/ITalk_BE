package com.chithanh.italk.web.endpoint.talk;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.talk.payload.request.ChallengeRequest;
import com.chithanh.italk.talk.payload.response.ChallengeResponse;
import com.chithanh.italk.talk.payload.response.SubmissionResponse;
import com.chithanh.italk.talk.service.ChallengeService;
import com.chithanh.italk.talk.service.SubmissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Api(tags = "Challenge APIs")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final SubmissionService submissionService;

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
        Page<ChallengeResponse> pageChallenges = challengeService.getAllChallenges(pageable);
        PageInfo pageInfo = new PageInfo(
                pageChallenges.getNumber() + 1,
                pageChallenges.getTotalPages(),
                pageChallenges.getTotalElements()
        );
        return ResponseEntity.ok(ResponseDataAPI.success(pageChallenges.getContent(), pageInfo));
    }

    @PostMapping("/challenges")
    @ApiOperation("create a new challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDataAPI> createChallenge(@Valid @RequestBody ChallengeRequest challengeRequest)
    {
        ChallengeResponse response = challengeService.createChallenge(challengeRequest);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
    }
    @PostMapping("/challenges/bulk")
    @ApiOperation("create multiple challenges")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDataAPI> createChallenges(@Valid @RequestBody List<ChallengeRequest> requests) {
        List<ChallengeResponse> responses = challengeService.createChallenges(requests);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(responses));
    }

    @PatchMapping("/challenges/{challengeId}")
    @ApiOperation("update a challenge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDataAPI> updateChallenge(
            @PathVariable UUID challengeId,
            @RequestBody ChallengeRequest challengeRequest
    ) {
        ChallengeResponse response =  challengeService.updateChallenge(challengeId, challengeRequest);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
    }
    @GetMapping("/today-challenge")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> getTodayChallengeOrSubmission(
            @CurrentUser UserPrincipal userPrincipal
            ) {
        UUID userId = userPrincipal.getId();
        if( submissionService.findTodaySubmission(userId) != null) {
            SubmissionResponse submission =  submissionService.findTodaySubmission(userId);
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(submission));
        } else {
            List<UUID> completedChallengeIds = submissionService.findChallengeIdsByUserId(userId);
            ChallengeResponse randomChallenge = challengeService.getRandomChallenge(userId, completedChallengeIds);
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(randomChallenge));
        }
    }

}