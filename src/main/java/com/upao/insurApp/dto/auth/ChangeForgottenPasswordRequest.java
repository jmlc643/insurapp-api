package com.upao.insurApp.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeForgottenPasswordRequest {
    @NotBlank(message = "La contraseña no puede ser un espacio en blanco")
    @NotEmpty(message = "La contraseña no puede estar vacia")
    @Size(message = "La contraseña debe tener al menos 8 carácteres", min = 8)
    private String password;
    @NotBlank(message = "La contraseña no puede ser un espacio en blanco")
    @NotEmpty(message = "La contraseña no puede estar vacia")
    @Size(message = "La contraseña debe tener al menos 8 carácteres", min = 8)
    private String confirmationPassword;
    @NotBlank(message = "El código no puede ser un espacio en blanco")
    @NotEmpty(message = "El código no puede estar vacio")
    private String code;
}
