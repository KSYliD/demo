package com.example.demo.service.impl;

import com.example.demo.dto.FeedbackDto;
import com.example.demo.model.Feedback;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.service.interfaces.FeedbackFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackFilterServiceImpl implements FeedbackFilterService {

    private final FeedbackRepository feedbackRepository;

    public List<FeedbackDto> filterFeedback(String branch, String position, String criticalityLevel) {
        return feedbackRepository.findAll().stream()
                .filter(f -> branch == null
                        || (f.getUserProfile() != null
                        && f.getUserProfile().getBranch() != null
                        && branch.equalsIgnoreCase(f.getUserProfile().getBranch().getName())))
                .filter(f -> position == null
                        || (f.getUserProfile() != null
                        && f.getUserProfile().getRole() != null
                        && position.equalsIgnoreCase(f.getUserProfile().getRole())))
                .filter(f -> criticalityLevel == null
                        || (f.getCriticality() != null
                        && criticalityLevel.equalsIgnoreCase(f.getCriticality().toString())))
                .map(this::toDto)
                .toList();
    }


    private FeedbackDto toDto(Feedback f) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(f.getId());
        dto.setText(f.getText());
        dto.setSentiment(f.getSentiment() != null ? f.getSentiment().name() : null);
        dto.setCriticality(f.getCriticality());
        dto.setSuggestedSolution(f.getSuggestedSolution());
        dto.setCreatedAt(f.getCreatedAt());
        dto.setGoogleDocUrl(f.getGoogleDocUrl());
        dto.setTrelloCardUrl(f.getTrelloCardUrl());

        if (f.getUserProfile() != null) {
            dto.setRole(f.getUserProfile().getRole());
            if (f.getUserProfile().getBranch() != null) {
                dto.setBranchName(f.getUserProfile().getBranch().getName());
            }
        }

        return dto;
    }

}
