package com.learn.block_academy.dto;

import lombok.Data;

@Data
public class LessonProgressRequest {
    private Long enrollmentId;
    private boolean completed;
    private int timeSpentSeconds;
}
