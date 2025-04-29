package com.upao.insurApp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserRequest {
    private String name;
    private String surname;
    private String dni;
    private String phone;
    private String email;
    private String password;
}
