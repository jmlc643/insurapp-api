package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.auth.AuthRequestDTO;
import com.upao.insurApp.dto.auth.AuthResponseDTO;
import com.upao.insurApp.dto.auth.ChangeForgottenPasswordRequest;
import com.upao.insurApp.dto.user.PasswordForgottenRequest;
import com.upao.insurApp.dto.user.RegisterUserRequest;
import com.upao.insurApp.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequest request) throws MessagingException {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/validate-code/{code}")
    public ResponseEntity<Void> validateCode(@PathVariable String code) {
        return new ResponseEntity<>(authService.validateCode(code), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @PostMapping("/password-forgotten")
    public ResponseEntity<Void> passwordForgotten(@RequestBody @Valid PasswordForgottenRequest request) throws MessagingException {
        return new ResponseEntity<>(authService.passwordForgotten(request), HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changeForgottenPassword(@RequestBody @Valid ChangeForgottenPasswordRequest request) {
        return new ResponseEntity<>(authService.changeForgottenPassword(request), HttpStatus.NO_CONTENT);
    }
}