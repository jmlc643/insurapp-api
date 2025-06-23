package com.upao.insurApp.units;

import com.upao.insurApp.dto.user.ProfileDTO;
import com.upao.insurApp.dto.user.UpdatePasswordRequest;
import com.upao.insurApp.dto.user.UpdateUserRequest;
import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.ERole;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;
    @Mock private PasswordEncoder passwordEncoder;

    @Test
    void testGetProfile() {
        // Given
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock static method
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock other dependencies
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(user.getUserId().toString());
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

            // When
            ProfileDTO result = userService.getProfile();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Jose Maria");
            assertThat(result.getPhone()).isEqualTo("956852465");
        }
    }

    @Test
    void testUpdateProfile() {
        // Given
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Frank", "Cortez", "978546231");
        User updatedUser = new User(1, "Frank", "Cortez", "12345678", "978546231", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock static method
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock other dependencies
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(user.getUserId().toString());
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);

            // When
            userService.updateProfile(updateUserRequest);

            // Then
            verify(userRepository, times(1)).save(any(User.class));
            assertThat(updatedUser.getName()).isEqualTo("Frank");
            assertThat(updatedUser.getSurname()).isEqualTo("Cortez");
            assertThat(updatedUser.getPhone()).isEqualTo("978546231");
        }
    }

    @Test
    void testUpdateProfileWhenPhoneHasNotNineDigits() {
        // Given
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Frank", "Cortez", "9785");

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock static method
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock other dependencies
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(user.getUserId().toString());
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

            // When
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.updateProfile(updateUserRequest));

            // Then
            assertThat(ex.getMessage()).isEqualTo("El número de teléfono debe tener 9 dígitos");

        }
    }

    @Test
    void testUpdateProfileWhenPhoneHasNotStartWithNine() {
        // Given
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Frank", "Cortez", "123456789");

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock static method
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock other dependencies
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(user.getUserId().toString());
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

            // When
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.updateProfile(updateUserRequest));

            // Then
            assertThat(ex.getMessage()).isEqualTo("El número de teléfono debe empezar por 9");

        }
    }

    @Test
    void testUpdatePassword() {
        // Given
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());
        UpdatePasswordRequest updateUserRequest = new UpdatePasswordRequest("hola1234", "hola1234");
        User updatedUser = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "encodedPassword", ERole.USER, new ArrayList<>());

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock static method
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock other dependencies
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(user.getUserId().toString());
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
            when(passwordEncoder.encode(updateUserRequest.getPassword())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);

            // When
            userService.updatePassword(updateUserRequest);

            // Then
            verify(userRepository, times(1)).save(any(User.class));
            assertThat(updatedUser.getPassword()).isEqualTo("encodedPassword");
        }
    }

    @Test
    void testUpdatePasswordWhenBothPasswordNotMatch() {
        // Given
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());
        UpdatePasswordRequest updateUserRequest = new UpdatePasswordRequest("hola1234", "hola12345");

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock static method
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock other dependencies
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(user.getUserId().toString());
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

            // When
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(updateUserRequest));

            // Then
            assertThat(ex.getMessage()).isEqualTo("Las contraseñas no coinciden o son inválidas");
        }
    }
}
