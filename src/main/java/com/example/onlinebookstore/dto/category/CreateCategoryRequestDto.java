package com.example.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateCategoryRequestDto {
    @NotNull
    @Length(min = 1, max = 255)
    private String name;
    @Length(min = 1, max = 255)
    private String description;
}
