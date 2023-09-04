package com.example.onlinebookstore.dto.order;

import com.example.onlinebookstore.enums.Status;
import lombok.Data;

@Data
public class UpdateStatusOrderDto {
    private Status status;
}
