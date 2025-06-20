package com.upao.insurApp.services;

import com.upao.insurApp.dto.user.ProfileDTO;
import com.upao.insurApp.dto.user.UpdatePasswordRequest;
import com.upao.insurApp.dto.user.UpdateUserRequest;
import com.upao.insurApp.exceptions.ResourceNotExistsException;
import com.upao.insurApp.models.User;
import com.upao.insurApp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public ProfileDTO getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotExistsException("El usuario no existe"));
        return new ProfileDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhone(), user.getDni());
    }

    public Void updatePassword(UpdatePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotExistsException("El usuario no existe"));
        if (!request.isValid()) {
            throw new IllegalArgumentException("Las contraseñas no coinciden o son inválidas");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return null;
    }

    public Void updateProfile(UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotExistsException("El usuario no existe"));
        if (!request.getName().isEmpty() && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (!request.getSurname().isEmpty() && !request.getSurname().isBlank()) {
            user.setSurname(request.getSurname());
        }
        if (!request.getPhone().isEmpty() && !request.getPhone().isBlank()) {
            if (!request.getPhone().matches("\\d{9}")) {
                throw new IllegalArgumentException("El número de teléfono debe tener 9 dígitos");
            }
            if (!request.getPhone().startsWith("9")) {
                throw new IllegalArgumentException("El número de teléfono debe empezar por 9");
            }
            user.setPhone(request.getPhone());
        }
        userRepository.save(user);
        return null;
    }
}