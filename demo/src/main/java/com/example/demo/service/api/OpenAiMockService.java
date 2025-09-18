package com.example.demo.service.api;

import com.example.demo.dto.FeedbackAnalysis;
import com.example.demo.model.Sentiment;
import org.springframework.stereotype.Service;

@Service
public class OpenAiMockService implements OpenAiService {

    @Override
    public FeedbackAnalysis analyzeFeedback(String message) {
        String lower = message.toLowerCase();

        String sentiment = "NEUTRAL";
        int criticality = 3;

        if (lower.contains("поганий") || lower.contains("затримка") || lower.contains("проблема")) {
            sentiment = "NEGATIVE";
            criticality = 4;
        } else if (lower.contains("новий") || lower.contains("хороший") || lower.contains("дякую")) {
            sentiment = "POSITIVE";
            criticality = 2;
        }

        return new FeedbackAnalysis(Sentiment.valueOf(sentiment), criticality);
    }
}

