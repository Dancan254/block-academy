package com.learn.block_academy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDateTime enrollmentDate;

    private boolean completed;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private List<LessonProgress> progress = new ArrayList<>();

    private BigDecimal pricePaid;

    private String paymentTransactionId;

    @PrePersist
    protected void onCreate() {
        enrollmentDate = LocalDateTime.now();
    }
}
