package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long telegramUserId;

    @Column(nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private Instant createdAt = Instant.now();
}

