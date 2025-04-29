package com.upao.insurApp.services;

import com.upao.insurApp.dto.auth.AuthRequestDTO;
import com.upao.insurApp.dto.auth.AuthResponseDTO;
import com.upao.insurApp.dto.auth.ChangeForgottenPasswordRequest;
import com.upao.insurApp.dto.email.Mail;
import com.upao.insurApp.dto.user.PasswordForgottenRequest;
import com.upao.insurApp.dto.user.RegisterUserRequest;
import com.upao.insurApp.exceptions.*;
import com.upao.insurApp.models.Code;
import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.ERole;
import com.upao.insurApp.models.enums.EStatus;
import com.upao.insurApp.models.enums.TypeCode;
import com.upao.insurApp.repos.CodeRepository;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.utils.JwtUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private CodeRepository codeRepository;
    @Autowired private JwtDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Value("${email.sender}")
    private String mailFrom;

    public Void register(RegisterUserRequest request) throws MessagingException {
        User user = new User(
                null,
                request.getName(),
                request.getSurname(),
                request.getDni(),
                request.getPhone(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                ERole.USER);
        if(userRepository.existsByEmail(request.getEmail())) throw new ResourceAlreadyExistsException("El email ya ha sido usado para la creación de otro usuario");
        userRepository.save(user);
        sendEmail(user, TypeCode.ACTIVATE,"Activa tu cuenta", "email/activate-user-email-template");
        return null;
    }

    public Void validateCode(String code) {
        Code lastCode = codeRepository.findByCode(code).orElseThrow(() -> new ResourceNotExistsException("El codigo enviado no existe"));
        if (lastCode.getStatus() == EStatus.EXPIRED || lastCode.getStatus() == EStatus.VERIFIED) {
            throw new ExpiredCodeException("El código ya fue utilizado o esta expirado");
        }
        if (lastCode.getExpiredDate().isBefore(LocalDateTime.now())) {
            lastCode.setStatus(EStatus.EXPIRED);
            codeRepository.save(lastCode);
            throw new ExpiredCodeException("El código ha expirado");
        }
        if(lastCode.getTypeCode() == TypeCode.ACTIVATE) {
            lastCode.setStatus(EStatus.VERIFIED);
            codeRepository.save(lastCode);
        }
        return null;
    }

    public AuthResponseDTO login(AuthRequestDTO request){
        Authentication authentication = authenticate(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateToken(authentication);
        jwtUtils.validateJWT(accessToken);
        return new AuthResponseDTO(accessToken);
    }

    public Void passwordForgotten(PasswordForgottenRequest request) throws MessagingException {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotExistsException("El usuario no fue encontrado"));
        sendEmail(user, TypeCode.RECOVERY, "Recupera tu contraseña", "email/password-forgotten-email-template");
        return null;
    }

    public Void changeForgottenPassword(ChangeForgottenPasswordRequest request) {
        if (!Objects.equals(request.getPassword(), request.getConfirmationPassword())) {
            throw new DifferentPasswordException("Las contraseñas no coinciden");
        }
        Code code = codeRepository.findByCode(request.getCode()).orElseThrow(() -> new ResourceNotExistsException("El codigo no fue encontrado"));
        if (code.getStatus() != EStatus.PENDING) {
            throw new ExpiredCodeException("El codigo ya fue utilizadoo expirado");
        }
        User user = code.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        code.setStatus(EStatus.VERIFIED);
        codeRepository.save(code);
        return null;
    }

    private Authentication authenticate(String email, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(userDetails == null){
            throw new BadCredentialsException("Usuario o contraseña inválida");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña inválida");
        }

        User user = userRepository.findById(Integer.valueOf(userDetails.getUsername())).orElseThrow(() -> new ResourceNotExistsException("No se encontró al usuario"));
        Code code = codeRepository.findFirstByUser(user).orElseThrow(() -> new ResourceNotExistsException("No se encontró el código"));

        if (code.getStatus() != EStatus.VERIFIED) {
            throw new UserNotActivatedException("La cuenta del usuario no está activada");
        }

        if (code.getTypeCode() != TypeCode.ACTIVATE) {
            throw new UserNotActivatedException("El codigo no es para activar cuenta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    private void sendEmail(User user, TypeCode typeCode, String subject, String template) throws MessagingException {
        Random random = new Random();
        Optional<Code> lastCode = codeRepository.findFirstByUserAndTypeCodeOrderByCodeIdDesc(user, typeCode);
        lastCode.ifPresent(code -> codeRepository.delete(code));
        String code = String.format("%06d", random.nextInt(100000));
        Code codeEntity = new Code(null, code, typeCode, EStatus.PENDING, LocalDateTime.now().plusMinutes(10), user);
        codeRepository.save(codeEntity);
        Map<String, Object> model = new HashMap<>();
        model.put("code", code);
        model.put("user", user.getName() + " " + user.getSurname());
        Mail mail = emailService.createMail(user.getEmail(), subject, model, mailFrom);
        emailService.sendEmail(mail, template);
    }
}
