package com.chithanh.italk.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface GeminiService {
    String getFeedback(String topic, MultipartFile audio);
}