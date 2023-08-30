package com.example.onlinebookstore.dto.user;

import com.example.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!")
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
    @NotNull
    private String password;
    @Length(min = 4,max = 255)
    @NotNull
    private String repeatPassword;
    @Length(min = 1,max = 255)
    private String shippingAddress;
}
