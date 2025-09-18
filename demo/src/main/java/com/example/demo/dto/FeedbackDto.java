package com.example.demo.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class FeedbackDto {
    private Long id;
    private String text;
    private String sentiment;
    private Integer criticality;
    private String suggestedSolution;
    private Instant createdAt;
    private String googleDocUrl;
    private String trelloCardUrl;
    private String branchName;
    private String role;
}
