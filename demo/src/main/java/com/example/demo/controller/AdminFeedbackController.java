package com.example.demo.controller;

import com.example.demo.dto.FeedbackDto;
import com.example.demo.service.interfaces.FeedbackFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminFeedbackController {

    private final FeedbackFilterService feedbackFilterService;

    @GetMapping
    public List<FeedbackDto> getFeedback(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String criticalityLevel
    ) {
        return feedbackFilterService.filterFeedback(branch, position, criticalityLevel);
    }
}
