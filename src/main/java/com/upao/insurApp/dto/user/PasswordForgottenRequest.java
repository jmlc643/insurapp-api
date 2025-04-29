package com.upao.insurApp.dto.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordForgottenRequest {
    @Email(message = "Formato incorrecto")
    private String email;
}
