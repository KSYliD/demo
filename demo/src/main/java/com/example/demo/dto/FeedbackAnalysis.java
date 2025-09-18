package com.example.demo.dto;

import com.example.demo.model.Sentiment;

public class FeedbackAnalysis {
    private Sentiment sentiment;
    private int criticality;

    public FeedbackAnalysis(Sentiment sentiment, int criticality) {
        this.sentiment = sentiment;
        this.criticality = criticality;
    }

    public Sentiment getSentiment() { return sentiment; }
    public int getCriticality() { return criticality; }
}
