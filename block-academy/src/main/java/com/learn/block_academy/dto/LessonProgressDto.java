package com.learn.block_academy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgressDto {
    private Long id;
    private Long lessonId;
    private Long enrollmentId;
    private boolean completed;
    private LocalDateTime lastAccessDate;
    private int timeSpentSeconds;
}
