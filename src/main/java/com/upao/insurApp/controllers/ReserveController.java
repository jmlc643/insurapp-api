package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.reserve.ReserveRequestDTO;
import com.upao.insurApp.dto.reserve.ReserveResponseDTO;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.User;
import com.upao.insurApp.repos.FieldRepository;
import com.upao.insurApp.repos.ReserveRepository;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.services.ReserveService;
import com.upao.insurApp.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReserveController {

    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    private final ReserveService reserveService;
    private final JwtUtils jwtUtils;

    public ReserveController(ReserveRepository reserveRepository, UserRepository userRepository, FieldRepository fieldRepository, ReserveService reserveService, JwtUtils jwtUtils) {
        this.reserveRepository = reserveRepository;
        this.userRepository = userRepository;
        this.fieldRepository = fieldRepository;
        this.reserveService = reserveService;
        this.jwtUtils = jwtUtils;
    }

    // Lista la reserva por ID
    @GetMapping("/{reserveId}")
    public ResponseEntity<ReserveResponseDTO> getReserveById(@PathVariable Integer reserveId) {
        Optional<Reserve> reserve = reserveRepository.findById(reserveId);
        return reserve.map(r -> ResponseEntity.ok(new ReserveResponseDTO(r)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crea la reserva y se valida si hay o no disponibilidad para tal hora
    @PostMapping("/createReserve")
    public ResponseEntity<?> createReservation(@RequestBody ReserveRequestDTO dto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Field> fieldOpt = fieldRepository.findById(dto.getFieldId());

        if (userOpt.isEmpty() || fieldOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user or field");
        }

        // Validar solapamiento de horarios
        if (!reserveService.isTimeSlotAvailable(dto.getFieldId(), dto.getBookingDate(), dto.getTimetableStart(), dto.getTimetableEnd())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Horario no disponible.");
        }

        Reserve reserve = reserveService.mapToReserve(dto, userOpt.get(), fieldOpt.get());
        Reserve saved = reserveService.createReservation(reserve);
        return ResponseEntity.ok(new ReserveResponseDTO(saved));
    }

    @PatchMapping("/validate/{id}")
    public ResponseEntity<?> validateReservation(@PathVariable Integer id, Authentication authentication) {
        // Verificar que el usuario tenga el rol ADMIN
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para validar esta reserva.");
        }

        try {
            Reserve validatedReserve = reserveService.validateReservation(id);
            return ResponseEntity.ok(new ReserveResponseDTO(validatedReserve));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

