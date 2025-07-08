package com.chithanh.italk.talk.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.BadRequestException;
import com.chithanh.italk.common.exception.ForbiddenException;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.talk.domain.Post;
import com.chithanh.italk.talk.domain.Submission;
import com.chithanh.italk.talk.domain.enums.PostType;
import com.chithanh.italk.talk.payload.request.PostRequest;
import com.chithanh.italk.talk.payload.response.PostResponse;
import com.chithanh.italk.talk.payload.response.SubmissionResponse;
import com.chithanh.italk.talk.payload.response.UserInfoResponse;
import com.chithanh.italk.talk.repository.PostRepository;
import com.chithanh.italk.talk.service.PostService;
import com.chithanh.italk.talk.service.SubmissionService;
import com.chithanh.italk.user.domain.UserInfo;
import com.chithanh.italk.user.payload.response.UserResponse;
import com.chithanh.italk.user.service.UserInfoService;
import com.chithanh.italk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SubmissionService submissionService;
    private final UserInfoService userInfoService;
    private final UserService userService;

    @Override
    public PostResponse createPost(UUID userId, PostRequest request) {
        User user = userService.findById(userId);
        Post post = new Post();
        if(request.getType().equals(PostType.SUBMISSION)) {
            if(request.getSubmissionId() == null){
                throw new BadRequestException(MessageConstant.SUBMISSION_ID_REQUIRED);
            }
            if(existsBySubmissionId(request.getSubmissionId())){
                throw new BadRequestException(MessageConstant.SUBMISSION_ALREADY_EXISTS);
            }
            post.setSubmission(submissionService.findById(request.getSubmissionId()));
        }
        post.setContent(request.getContent());
        post.setUser(user);
        post.setType(request.getType());
        Post savedPost = postRepository.save(post);
        return PostResponse.toResponse(savedPost,
                SubmissionResponse.builder()
                        .id(savedPost.getSubmission().getId())
                .build(),
                UserInfoResponse.builder()
                        .userId(user.getId())
                        .build());
    }

    @Override
    public PostResponse updatePost(UUID postId, PostRequest request, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.POST_NOT_FOUND));
        if (!post.getUser().getId().equals(userId)) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        postRepository.save(post);
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .type(post.getType())
                .submission(post.getSubmission() != null ? SubmissionResponse.builder()
                        .id(post.getSubmission().getId())
                        .challengeId(post.getSubmission().getChallenge().getId())
                        .question(post.getSubmission().getChallenge().getQuestion())
                        .audioUrl(post.getSubmission().getAudioUrl())
                        .score(post.getSubmission().getScore())
                        .build() : null)
                .build();
        return postResponse;
    }

    @Override
    public Page<PostResponse> getPostsByType(PostType type, Pageable pageable) {
        Page<Post> posts = postRepository.findByType(type, pageable);
        if (posts.isEmpty()) {
            return Page.empty(pageable);
        }
        List<UUID> userIds = posts.stream()
                .map(post -> post.getUser().getId())
                .distinct()
                .toList();
        List<UserInfo> userInfos = userInfoService.getUserInfosByUserIds(userIds);
        Map<UUID,UserInfo> userInfoMap = userInfos.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, Function.identity()));
        return posts.map(post -> {
            SubmissionResponse submissionResponse = null;
            if(post.getSubmission() !=null){
                Submission submission = post.getSubmission();
                submissionResponse = SubmissionResponse.builder()
                        .id(submission.getId())
                        .challengeId(submission.getChallenge().getId())
                        .question(submission.getChallenge().getQuestion())
                        .audioUrl(submission.getAudioUrl())
                        .score(submission.getScore())
                        .build();
            }
            User user = post.getUser();
            UserInfo userInfo =userInfoMap.get(user.getId());
            return PostResponse.builder()
                    .id(post.getId())
                    .type(post.getType())
                    .submission(submissionResponse)
                    .user(UserInfoResponse.toResponse(userInfo))
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .build();
        });
    }
    private boolean existsBySubmissionId(UUID submissionId) {
        return postRepository.existsBySubmissionId(submissionId);
    }
}