package com.learn.block_academy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDto {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private int order;
    private List<LessonDto> lessons;
}
