package com.example.demo.service.api;

import com.example.demo.dto.FeedbackAnalysis;

public interface OpenAiService {
    FeedbackAnalysis analyzeFeedback(String message);
}
