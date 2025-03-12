package com.learn.block_academy.dto;


import com.learn.block_academy.enums.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    private BigDecimal price;

    @NotNull(message = "Course level is required")
    private CourseLevel level;
}
