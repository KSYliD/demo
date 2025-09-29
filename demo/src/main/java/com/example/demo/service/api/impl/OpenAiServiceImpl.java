package com.example.demo.service.api.impl;

import com.example.demo.dto.FeedbackAnalysis;
import com.example.demo.model.Sentiment;
import com.example.demo.service.api.interfaces.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public FeedbackAnalysis analyzeFeedback(String feedbackText) {
        String url = "https://api.openai.com/v1/chat/completions";

        Map<String, Object> request = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are a helpful assistant that analyzes employee feedback."),
                        Map.of("role", "user", "content",
                                "Analyze this feedback:\n" + feedbackText +
                                        "\nReturn JSON with fields: sentiment (NEGATIVE, NEUTRAL, POSITIVE), " +
                                        "criticality (1-5), suggestedSolution (string).")
                }
        );

        var response = restTemplate.postForObject(
                url,
                new org.springframework.http.HttpEntity<>(request, getHeaders()),
                Map.class
        );

        String content = (String) ((Map)((Map)((java.util.List)response.get("choices")).get(0)).get("message")).get("content");

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try {
            Map<String, Object> result = mapper.readValue(content, Map.class);

            FeedbackAnalysis analysis = new FeedbackAnalysis();
            analysis.setSentiment(Sentiment.valueOf((String) result.get("sentiment")));
            analysis.setCriticality((Integer) result.get("criticality"));
            analysis.setSuggestedSolution((String) result.get("suggestedSolution"));
            return analysis;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response: " + content, e);
        }
    }

    private org.springframework.http.HttpHeaders getHeaders() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}

