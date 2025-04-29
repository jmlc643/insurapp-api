package com.upao.insurApp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDTO {
    @NotNull(message = "El email no puede ser null")
    @NotEmpty(message = "El email no puede estar vacio")
    @NotBlank(message = "El email no puede ser un espacio en blanco")
    @Email(message = "Formato incorrecto")
    String email;
    @NotNull(message = "La contraseña no puede ser null")
    @NotEmpty(message = "La contraseña no puede estar vacia")
    @NotBlank(message = "La contraseña no puede ser un espacio en blanco")
    String password;
}