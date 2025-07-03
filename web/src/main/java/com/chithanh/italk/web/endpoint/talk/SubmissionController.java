package com.chithanh.italk.web.endpoint.talk;

import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.talk.domain.Submission;
import com.chithanh.italk.talk.mapper.SubmissionMapper;
import com.chithanh.italk.talk.service.SubmissionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;

    @PostMapping("/challenges/{challengeId}/users/{userId}/submissions")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("submit a challenge")
    public ResponseEntity<ResponseDataAPI> submitChallenge(
            @PathVariable("challengeId") UUID challengeId,
            @PathVariable("userId") UUID userId,
            @RequestParam MultipartFile audio
    ) {
        Submission submission = submissionService.submit(
                challengeId, userId, audio);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(submissionMapper.toSubmissionResponse(submission)));
    }

}