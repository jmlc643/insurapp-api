package com.upao.insurApp.units;

import com.upao.insurApp.dto.user.ProfileDTO;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    @Test
    void getProfile() {
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
}
