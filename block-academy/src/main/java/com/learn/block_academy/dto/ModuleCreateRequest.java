package com.learn.block_academy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @PositiveOrZero(message = "Order must be positive or zero")
    private int order;
}
