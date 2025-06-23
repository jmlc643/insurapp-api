package com.upao.insurApp.controllers;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.upao.insurApp.dto.reserve.ReserveDTO;
import com.upao.insurApp.dto.reserve.ReserveRequestDTO;
import com.upao.insurApp.dto.reserve.ReserveResponseDTO;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.RStatus;
import com.upao.insurApp.repos.FieldRepository;
import com.upao.insurApp.repos.ReserveRepository;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.services.ReserveService;
import com.upao.insurApp.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        reserve.setStatus(RStatus.PENDING);
        Reserve saved = reserveService.createReservation(reserve);
        return ResponseEntity.ok(new ReserveDTO(saved));
    }

    // Se valida como rol ADMIN la reserva
    @PatchMapping("/validate/{id}")
    public ResponseEntity<?> validateReservation(@PathVariable Integer id) {
        try {
            Reserve validatedReserve = reserveService.validateReservation(id);
            return ResponseEntity.ok(new ReserveResponseDTO(validatedReserve));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Muestra la info al escanear el código QR
    @GetMapping("/validate-info")
    public ResponseEntity<?> getReservationInfoFromQr(@RequestParam("id") Integer reserveId) {
        Optional<Reserve> reserveOpt = reserveRepository.findById(reserveId);

        if (reserveOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada.");
        }

        ReserveDTO dto = new ReserveDTO(reserveOpt.get());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<Void> updateReservationStatus(@PathVariable Integer id, @RequestParam("status") String status) {
        return new ResponseEntity<>(reserveService.updateReservationStatus(id, status), HttpStatus.NO_CONTENT);
    }

}

