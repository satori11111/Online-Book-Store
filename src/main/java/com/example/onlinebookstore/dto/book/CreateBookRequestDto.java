package com.example.onlinebookstore.dto.book;

import com.example.onlinebookstore.validation.CoverImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Length(min = 1,max = 255)
    private String title;
    @NotNull
    @Length(min = 1,max = 255)
    private String author;
    @NotNull
    @ISBN
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @Length(min = 1,max = 255)
    private String description;
    @CoverImage
    private String coverImage;
    @Size(min = 1)
    private Set<Long> categoryIds;
}
