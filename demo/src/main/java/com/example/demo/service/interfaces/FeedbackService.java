package com.example.demo.service.interfaces;

import com.example.demo.model.Feedback;
import com.example.demo.model.UserProfile;
import org.springframework.data.domain.Page;

public interface FeedbackService {
    void handleNewFeedback(UserProfile userProfile, String message);
    Page<Feedback> findFiltered(String branch, String position, String level, int page, int size);
}
