package com.example.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateQuantityCartItemDto {
    @NotNull
    @Min(0)
    private Integer quantity;
}
