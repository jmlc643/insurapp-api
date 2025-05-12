package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.user.ProfileDTO;
import com.upao.insurApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> profile() {
        return new ResponseEntity<>(userService.getProfile(), HttpStatus.OK);
    }
}
