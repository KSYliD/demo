package com.example.demo.service.impl;

import com.example.demo.dto.FeedbackAnalysis;
import com.example.demo.model.Feedback;
import com.example.demo.model.UserProfile;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.service.api.interfaces.GoogleSheetsService;
import com.example.demo.service.api.interfaces.OpenAiService;
import com.example.demo.service.api.interfaces.TrelloService;
import com.example.demo.service.interfaces.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final OpenAiService openAiService;
    private final FeedbackRepository feedbackRepository;
    private final GoogleSheetsService googleSheetsService;
    private final TrelloService trelloService;

    public void handleNewFeedback(UserProfile userProfile, String message) {
        FeedbackAnalysis analysis = openAiService.analyzeFeedback(message);

        Feedback feedback = new Feedback();
        feedback.setUserProfile(userProfile);
        feedback.setText(message);
        feedback.setSentiment(analysis.getSentiment());
        feedback.setCriticality(analysis.getCriticality());
        feedback.setSuggestedSolution(analysis.getSuggestedSolution());

        Feedback saved = feedbackRepository.save(feedback);

        try {
            String sheetUrl = googleSheetsService.appendFeedbackRow(saved);
            saved.setGoogleDocUrl(sheetUrl);

            if (saved.getCriticality() >= 4) {
                String trelloUrl = trelloService.createCardForFeedback(saved);
                saved.setTrelloCardUrl(trelloUrl);
            }

            feedbackRepository.save(saved);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Page<Feedback> findFiltered(String branch, String position, String level, int page, int size) {
        Specification<Feedback> spec = Specification.allOf();

        if (branch != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("userProfile").get("branch").get("name"), branch));
        }
        if (position != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("userProfile").get("role"), position));
        }
        if (level != null) {
            Integer crit = switch (level.toLowerCase()) {
                case "low" -> 1;
                case "medium" -> 3;
                case "high", "critical" -> 5;
                default -> null;
            };
            if (crit != null) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("criticality"), crit));
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        return feedbackRepository.findAll(spec, pageable);
    }

}
