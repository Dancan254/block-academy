package com.learn.block_academy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentDto {
    private Long id;
    private Long userId;
    private Long courseId;
    private String enrollmentDate;
}
