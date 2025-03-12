package com.learn.block_academy.model;

import com.learn.block_academy.enums.LessonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    @Column(length = 5000)
    private String content;

    private String videoUrl;

    private String attachmentUrl;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(name = "lesson_order")
    private int order;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @Builder.Default
    private List<LessonProgress> progress = new ArrayList<>();
}

