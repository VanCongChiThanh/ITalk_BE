package com.chithanh.italk.talk.service;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.talk.domain.Post;
import com.chithanh.italk.talk.domain.enums.PostType;
import com.chithanh.italk.talk.payload.request.PostRequest;
import com.chithanh.italk.talk.payload.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PostService {
    PostResponse createPost(User user, PostRequest request);
    PostResponse updatePost(UUID postId, PostRequest request);
    Page<PostResponse> getPostsByType(PostType type, Pageable pageable);
}