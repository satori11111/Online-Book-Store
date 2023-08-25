package com.example.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegistrationRequestDto {
    @NotNull
    @Length(min = 1,max = 255)
    private String firstName;
    @NotNull
    @Length(min = 1,max = 255)
    private String lastName;
    @NotNull
    @Email
    private String email;
    @Length(min = 4,max = 255)
    private String password;
    @Length(min = 1,max = 255)
    private String shippingAddress;
}
