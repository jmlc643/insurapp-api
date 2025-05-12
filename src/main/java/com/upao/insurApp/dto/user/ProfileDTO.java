package com.upao.insurApp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String dni;
}
