package com.example.demo.service.interfaces;

import com.example.demo.dto.FeedbackDto;
import java.util.List;

public interface FeedbackFilterService {
    public List<FeedbackDto> filterFeedback(String branch, String position, String criticalityLevel);
}
