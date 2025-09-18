package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    private Integer criticality;

    @Column(columnDefinition = "text")
    private String suggestedSolution;

    private Instant createdAt = Instant.now();

    private String googleDocUrl;
    private String trelloCardUrl;
}
