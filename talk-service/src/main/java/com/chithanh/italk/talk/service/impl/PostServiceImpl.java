package com.chithanh.italk.talk.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.BadRequestException;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.talk.domain.Post;
import com.chithanh.italk.talk.domain.Submission;
import com.chithanh.italk.talk.domain.enums.PostType;
import com.chithanh.italk.talk.payload.request.PostRequest;
import com.chithanh.italk.talk.payload.response.PostResponse;
import com.chithanh.italk.talk.payload.response.SubmissionResponse;
import com.chithanh.italk.talk.repository.PostRepository;
import com.chithanh.italk.talk.service.PostService;
import com.chithanh.italk.talk.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SubmissionService submissionService;

    @Override
    public PostResponse createPost(User user, PostRequest request) {
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
        PostResponse postResponse = PostResponse.builder()
                .id(savedPost.getId())
                .content(savedPost.getContent())
                .submission(savedPost.getSubmission() != null ? SubmissionResponse.builder()
                        .id(savedPost.getSubmission().getId())
                        .challengeId(savedPost.getSubmission().getChallenge().getId())
                        .build() : null)
                .type(savedPost.getType())
                .build();
        return postResponse;
    }

    @Override
    public PostResponse updatePost(UUID postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.POST_NOT_FOUND));
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
            return PostResponse.builder()
                    .id(post.getId())
                    .type(post.getType())
                    .submission(submissionResponse)
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .build();
        });
    }
    private boolean existsBySubmissionId(UUID submissionId) {
        return postRepository.existsBySubmissionId(submissionId);
    }
}