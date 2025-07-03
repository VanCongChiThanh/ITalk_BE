package com.chithanh.italk.ai.service.impl;

import com.chithanh.italk.ai.config.GeminiConfigProperties;
import com.chithanh.italk.ai.service.GeminiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@EnableConfigurationProperties(GeminiConfigProperties.class)
public class GeminiServiceImpl implements GeminiService {
    private final GeminiConfigProperties geminiConfigProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public String getFeedback(String topic, MultipartFile audio) {
        String transcript = "This is a placeholder transcript.";
        String prompt = String.format(
                "You are an experienced English Speaking examiner.\n" +
                        "Your task is to evaluate the following speaking response.\n\n" +
                        "Topic: %s\n" +
                        "Transcript: %s\n\n" +
                        "Evaluate the response based on these 5 criteria:\n" +
                        "- Comprehensibility\n" +
                        "- Fluency\n" +
                        "- Grammar\n" +
                        "- Pronunciation\n" +
                        "- Vocabulary\n\n" +
                        "Each criterion should be graded on a scale of 0 to 100.\n" +
                        "Also calculate the average score (0-100) based on the 5 criteria.\n\n" +
                        "Return the result in EXACTLY the following JSON format:\n\n" +
                        "{\n" +
                        "  \"score\": <calculated average score>,\n" +
                        "  \"criteria\": [\n" +
                        "    {\"score\": <score 0-100>, \"title\": \"Comprehensibility\", \"description\": \"detailed description\"},\n" +
                        "    {\"score\": <score 0-100>, \"title\": \"Fluency\", \"description\": \"detailed description\"},\n" +
                        "    {\"score\": <score 0-100>, \"title\": \"Grammar\", \"description\": \"detailed description\"},\n" +
                        "    {\"score\": <score 0-100>, \"title\": \"Pronunciation\", \"description\": \"detailed description\"},\n" +
                        "    {\"score\": <score 0-100>, \"title\": \"Vocabulary\", \"description\": \"detailed description\"}\n" +
                        "  ],\n" +
                        "  \"suggestion\": \"actionable suggestion for improvement\"\n" +
                        "}\n\n" +
                        "Return ONLY valid JSON without any explanation or additional text.",
                topic, transcript
        );
        return callGeminiApi(prompt);
    }
    private String callGeminiApi(String prompt) {
        String url = String.format("%s/%s:generateContent?key=%s",
                geminiConfigProperties.getBaseUrl(),
                geminiConfigProperties.getModelName(),
                geminiConfigProperties.getApiKey()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Gemini yêu cầu: contents -> parts
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(body);

                String result = jsonNode
                        .path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();
                return result;

            } else {
                log.error("Gemini API failed with status: {}", response.getStatusCode());
                throw new RuntimeException("Gemini API failed with status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            throw new RuntimeException("Error calling Gemini API: " + e.getMessage());
        }
    }

}