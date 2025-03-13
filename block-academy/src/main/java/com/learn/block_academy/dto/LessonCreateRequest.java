package com.learn.block_academy.dto;

import lombok.Data;

@Data
public class LessonCreateRequest {
    private String title;
    private String content;
    private int order;
}