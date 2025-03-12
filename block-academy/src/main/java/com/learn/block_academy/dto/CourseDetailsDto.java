package com.learn.block_academy.dto;

import com.learn.block_academy.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDetailsDto {
    private Long id;
    private String title;
    private String description;
    private UserDto instructor;
    private BigDecimal price;
    private CourseLevel level;
    private List<ModuleDto> modules;
    private int enrollmentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
