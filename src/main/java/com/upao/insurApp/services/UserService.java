package com.upao.insurApp.services;

import com.upao.insurApp.dto.user.ProfileDTO;
import com.upao.insurApp.exceptions.ResourceNotExistsException;
import com.upao.insurApp.models.User;
import com.upao.insurApp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public ProfileDTO getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotExistsException("El usuario no existe"));
        return new ProfileDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhone(), user.getDni());
    }
}