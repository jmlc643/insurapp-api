package com.upao.insurApp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    private String password;
    private String confirmPassword;

    public boolean isValid() {
        return password != null && !password.isBlank() && password.equals(confirmPassword);
    }
}
