package com.example.demo.service.api.interfaces;

import com.example.demo.dto.FeedbackAnalysis;

public interface OpenAiService {
    FeedbackAnalysis analyzeFeedback(String message);
}
