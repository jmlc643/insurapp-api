package com.upao.insurApp.units;

import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.ERole;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.services.JwtDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class JwtDetailsServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private JwtDetailsService jwtDetailsService;

    @Test
    void testLoadUserByUsername() {
        // Given
        String email = "jluyoc1@upao.edu.pe";
        User user = new User(1, "Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025", ERole.USER, new ArrayList<>());

        // When
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails result = jwtDetailsService.loadUserByUsername(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("1");
        assertThat(result.getPassword()).isEqualTo("upao2025");
        assertThat(result.isEnabled()).isEqualTo(true);
        assertThat(result.getAuthorities().toString()).isEqualTo("[ROLE_USER]");
    }

    @Test
    void testLoadUserByUsernameWhenUserNotExists() {
        // Given
        String email = "EMAIL_NOT_EXISTS";

        // When
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> jwtDetailsService.loadUserByUsername(email));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El usuario no fue encontrado");
    }
}