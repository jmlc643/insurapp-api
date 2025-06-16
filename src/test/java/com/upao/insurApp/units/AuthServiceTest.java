package com.upao.insurApp.units;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.upao.insurApp.dto.auth.AuthRequestDTO;
import com.upao.insurApp.dto.auth.AuthResponseDTO;
import com.upao.insurApp.dto.email.Mail;
import com.upao.insurApp.dto.user.RegisterUserRequest;
import com.upao.insurApp.exceptions.ResourceAlreadyExistsException;
import com.upao.insurApp.models.Code;
import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.ERole;
import com.upao.insurApp.models.enums.EStatus;
import com.upao.insurApp.models.enums.TypeCode;
import com.upao.insurApp.repos.CodeRepository;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.services.AuthService;
import com.upao.insurApp.services.EmailService;
import com.upao.insurApp.services.JwtDetailsService;
import com.upao.insurApp.utils.JwtUtils;
import com.upao.insurApp.utils.UserSecurity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private AuthService authService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private JwtDetailsService jwtDetailsService;
    @Mock private SpringTemplateEngine templateEngine;
    @Mock private CodeRepository codeRepository;
    @Mock private EmailService emailService;

    private String mailFrom;
    private Code code;

    @BeforeEach
    void setUp() {
        mailFrom = "eprueba736@gmail.com";
        code = new Code(1, "123456", TypeCode.ACTIVATE, EStatus.PENDING, LocalDateTime.now().plusMinutes(10), null);
    }

    @Test
    void testRegister() {
        // Given
        RegisterUserRequest request = new RegisterUserRequest("Jose Maria", "Luyo", "12345678", "956852465", "jluyoc1@upao.edu.pe", "upao2025");
        User user = new User(null, request.getName(), request.getSurname(), request.getDni(), request.getPhone(), request.getEmail(), request.getPassword(), ERole.USER, new ArrayList<>());

        Map<String, Object> expectedModel = new HashMap<>();
        expectedModel.put("code", code);
        expectedModel.put("user", user.getName() + " " + user.getSurname());

        Mail mail = new Mail(mailFrom, user.getEmail(), "Activa tu cuenta", expectedModel);

        // When
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(emailService.createMail(anyString(), anyString(), anyMap(), anyString())).thenReturn(mail);

        authService.register(request);

        // Then
        verify(userRepository).save(any(User.class));
        verify(emailService).createMail(eq(user.getEmail()), eq("Activa tu cuenta"), eq(expectedModel), eq(mailFrom));
        verify(emailService).sendEmail(any(Mail.class), eq("email/activate-user-email-template"));
    }

    @Test
    void testRegisterWhenUsernameExists() {
        // Given
        RegisterUserRequest request = new RegisterUserRequest("name", "surname", "13245678", "13245678", "EXISTS_EMAIL", "password");

        // When
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El email ya ha sido usado para la creación de otro usuario");
    }

    @Test
    void testLogin() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO("email@example.com", "password");
        User user = new User(1, "Jose", "Luyo", "12345678", "123456789", "email@example.com", "password", ERole.USER);
        UserDetails userDetails = new UserSecurity(1, "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), user);
        Code code = new Code(1, "123456", TypeCode.ACTIVATE, EStatus.VERIFIED, LocalDateTime.now().plusMinutes(10), user);
        DecodedJWT decodedJWT = mock(DecodedJWT.class);

        // When
        when(jwtDetailsService.loadUserByUsername("email@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("password", userDetails.getPassword())).thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(codeRepository.findFirstByUser(any(User.class))).thenReturn(Optional.of(code));
        when(jwtUtils.generateToken(any(Authentication.class))).thenReturn("accessToken");
        when(jwtUtils.validateJWT("accessToken")).thenReturn(decodedJWT);

        AuthResponseDTO result = authService.login(request);

        // Assert
        Assertions.assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo("ROLE_USER");
        Assertions.assertThat(result.getToken()).isEqualTo("accessToken");
    }

    @Test
    void testLoginWhenJwtIsNotValid() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO("email@example.com", "password");
        User user = new User(1, "Jose", "Luyo", "12345678", "123456789", "email@example.com", "password", ERole.USER);
        UserDetails userDetails = new UserSecurity(1, "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), user);
        Code code = new Code(1, "123456", TypeCode.ACTIVATE, EStatus.VERIFIED, LocalDateTime.now().plusMinutes(10), user);

        // When
        when(jwtDetailsService.loadUserByUsername("email@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("password", userDetails.getPassword())).thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(codeRepository.findFirstByUser(any(User.class))).thenReturn(Optional.of(code));
        when(jwtUtils.generateToken(any(Authentication.class))).thenReturn("accessToken");
        doThrow(new JWTVerificationException("Token inválido, no estás autorizado")).when(jwtUtils).validateJWT(anyString());

        JWTVerificationException ex = assertThrows(JWTVerificationException.class, () -> authService.login(request));

        // Then
        Assertions.assertThat(ex.getMessage()).isEqualTo("Token inválido, no estás autorizado");
    }
}
