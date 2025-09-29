package com.example.demo.dto;

import com.example.demo.model.Sentiment;
import lombok.Data;

@Data
public class FeedbackAnalysis {
    private Sentiment sentiment;
    private int criticality;
    private String suggestedSolution;
}