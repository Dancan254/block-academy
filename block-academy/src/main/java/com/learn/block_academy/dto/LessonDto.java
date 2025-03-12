package com.learn.block_academy.dto;

import com.learn.block_academy.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDto {
    private Long id;
    private String title;
    private LessonType type;
    private String content;
    private String videoUrl;
    private String attachmentUrl;
    private Long moduleId;
    private int order;
    private boolean completed;
}
